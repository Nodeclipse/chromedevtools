// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.jetbrains.v8;

import org.chromium.sdk.*;
import org.chromium.sdk.internal.ScriptRegExpBreakpointTarget;
import org.chromium.sdk.internal.v8native.protocol.input.CommandResponse;
import org.chromium.sdk.internal.v8native.protocol.input.data.BreakpointInfo;
import org.chromium.sdk.util.GenericCallback;
import org.chromium.sdk.util.RelaySyncCallback;
import org.jetbrains.v8.protocol.Changebreakpoint;

/**
 * A generic implementation of the Breakpoint interface.
 */
public class BreakpointImpl implements Breakpoint {

  /**
   * The breakpoint target.
   */
  private Target target;

  /**
   * The breakpoint id as reported by the JavaScript VM.
   */
  private int id;

  /**
   * Breakpoint line number. May become invalidated by LiveEdit actions.
   */
  private int lineNumber;

  /**
   * Whether the breakpoint is enabled.
   */
  private boolean isEnabled;

  /**
   * The breakpoint condition (plain JavaScript) that should be {@code true}
   * for the breakpoint to fire.
   */
  private String condition;

  /**
   * The breakpoint manager that manages this breakpoint.
   */
  private final BreakpointManager breakpointManager;

  /**
   * Whether the breakpoint data have changed with respect
   * to the JavaScript VM data.
   */
  private volatile boolean isDirty = false;

  public BreakpointImpl(int id, Target target, int lineNumber, boolean enabled, String condition, BreakpointManager breakpointManager) {
    this.target = target;
    this.id = id;
    isEnabled = enabled;
    this.condition = condition;
    this.lineNumber = lineNumber;
    this.breakpointManager = breakpointManager;
  }

  public BreakpointImpl(BreakpointInfo info, BreakpointManager breakpointManager) {
    target = getType(info);
    id = info.number();
    this.breakpointManager = breakpointManager;
    updateFromRemote(info);
  }
  public void updateFromRemote(BreakpointInfo info) {
    if (id != info.number()) {
      throw new IllegalArgumentException();
    }
    lineNumber = info.line();
    isEnabled = info.active();
    condition = info.condition();
  }

  @Override
  public boolean isEnabled() {
    return isEnabled;
  }

  @Override
  public Target getTarget() {
    return target;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public String getCondition() {
    return condition;
  }

  @Override
  public int getLineNumber() {
    return lineNumber;
  }

  @Override
  public void setEnabled(boolean enabled) {
    if (isEnabled != enabled) {
      setDirty(true);
    }
    isEnabled = enabled;
  }

  private RelayOk setIgnoreCount(int ignoreCount, final GenericCallback<Void> callback, SyncCallback syncCallback) {
    V8CommandCallbackBase wrappedCallback;
    if (callback == null) {
      wrappedCallback = null;
    }
    else {
      wrappedCallback = new V8CommandCallbackBase() {
        @Override
        public void success(CommandResponse.Success successResponse) {
          callback.success(null);
        }

        @Override
        public void failure(String message) {
          callback.failure(new Exception(message));
        }
      };
    }
    return breakpointManager.getDebugSession().sendMessage(new Changebreakpoint(id).ignoreCount(ignoreCount), wrappedCallback, syncCallback);
  }

  @Override
  public void setCondition(String condition) {
    if (!eq(this.condition, condition)) {
      setDirty(true);
    }
    this.condition = condition;
  }

  private static <T> boolean eq(T left, T right) {
    return left == right || (left != null && left.equals(right));
  }

  @Override
  public RelayOk clear(JavascriptVm.BreakpointCallback callback, SyncCallback syncCallback) {
    // TODO: make this code thread-safe.
    int originalId = id;
    id = INVALID_ID;
    return breakpointManager.clearBreakpoint(this, callback, syncCallback, originalId);
  }

  @Override
  public RelayOk flush(JavascriptVm.BreakpointCallback callback, SyncCallback syncCallback) {
    if (!isDirty()) {
      if (callback != null) {
        callback.success(this);
      }
      return RelaySyncCallback.finish(syncCallback);
    }
    setDirty(false);
    return breakpointManager.changeBreakpoint(this, callback, syncCallback);
  }

  @Override
  public IgnoreCountBreakpointExtension getIgnoreCountBreakpointExtension() {
    JavascriptVm javascriptVm = breakpointManager.getDebugSession().getJavascriptVm();
    return javascriptVm.getIgnoreCountBreakpointExtension();
  }

  private void setDirty(boolean isDirty) {
    this.isDirty = isDirty;
  }

  private boolean isDirty() {
    return isDirty;
  }

  private static Target getType(BreakpointInfo info) {
    BreakpointInfo.Type infoType = info.type();
    switch (infoType) {
      case SCRIPTID: return new Target.ScriptId(info.script_id());
      case SCRIPTNAME: return new Target.ScriptName(info.script_name());
      case SCRIPTREGEXP: return new ScriptRegExpBreakpointTarget(info.script_regexp());
      case FUNCTION: return new FunctionTarget(null);
    }
    throw new RuntimeException("Unknown type: " + infoType);
  }

  /**
   * Visitor interface that includes all extensions.
   */
  public interface TargetExtendedVisitor<R> extends
      BreakpointTypeExtension.FunctionSupport.Visitor<R>,
      BreakpointTypeExtension.ScriptRegExpSupport.Visitor<R> {
  }

  static class FunctionTarget extends Target {
    private final String expression;
    FunctionTarget(String expression) {
      this.expression = expression;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      if (visitor instanceof BreakpointTypeExtension.FunctionSupport.Visitor) {
        BreakpointTypeExtension.FunctionSupport.Visitor<R> functionVisitor =
            (BreakpointTypeExtension.FunctionSupport.Visitor<R>) visitor;
        return functionVisitor.visitFunction(expression);
      } else {
        return visitor.visitUnknown(this);
      }
    }
  }

  static final IgnoreCountBreakpointExtension IGNORE_COUNT_EXTENSION =
      new IgnoreCountBreakpointExtension() {
    @Override
    public RelayOk setBreakpoint(JavascriptVm javascriptVm, Breakpoint.Target target,
        int line, int column,
        boolean enabled, String condition, int ignoreCount,
        JavascriptVm.BreakpointCallback callback, SyncCallback syncCallback) {
      JavascriptVmImpl javascriptVmImpl = (JavascriptVmImpl) javascriptVm;
      BreakpointManager breakpointManager =
          javascriptVmImpl.getDebugSession().getBreakpointManager();
      return breakpointManager.setBreakpoint(target, line, column, enabled, condition, ignoreCount,
          callback, syncCallback);
    }

    @Override
    public RelayOk setIgnoreCount(Breakpoint breakpoint, int ignoreCount,
        GenericCallback<Void> callback, SyncCallback syncCallback) {
      BreakpointImpl breakpointImpl = (BreakpointImpl) breakpoint;
      return breakpointImpl.setIgnoreCount(ignoreCount, callback, syncCallback);
    }
  };
}
