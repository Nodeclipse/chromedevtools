// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.jetbrains.v8;

import org.chromium.sdk.*;
import org.jetbrains.v8.processor.BacktraceProcessor;
import org.jetbrains.v8.protocol.PropertyNameGetter;
import org.jetbrains.v8.protocol.V8ProtocolUtil;
import org.chromium.sdk.internal.v8native.protocol.input.CommandResponse;
import org.chromium.sdk.internal.v8native.protocol.input.FrameObject;
import org.chromium.sdk.internal.v8native.protocol.input.ScopeRef;
import org.chromium.sdk.internal.v8native.protocol.output.BacktraceMessage;
import org.chromium.sdk.internal.v8native.protocol.output.RestartFrameMessage;
import org.jetbrains.v8.value.*;
import org.chromium.sdk.util.GenericCallback;
import org.chromium.sdk.util.MethodIsBlockingException;
import org.chromium.sdk.util.RelaySyncCallback;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A generic implementation of the CallFrame interface.
 */
public class CallFrameImpl implements CallFrame {

  /** The frame ID as reported by the JavaScript VM. */
  private final int frameId;

  /** The debug context this call frame belongs in. */
  private final InternalContext context;

  /** The underlying frame data from the JavaScript VM. */
  private final FrameObject frameObject;

  /**
   * 0-based line number in the entire script resource.
   */
  private final int lineNumber;

  /**
   * Function name associated with the frame.
   */
  private final String frameFunction;

  /**
   * The associated script id value.
   */
  private final int scriptId;

  /** The scopes known in this call frame. */
  private final AtomicReference<List<? extends JsScope>> scopesRef = new AtomicReference<List<? extends JsScope>>(null);

  /** The receiver variable known in this call frame. May be null. Null is not cached. */
  private final AtomicReference<JsVariable> receiverVariableRef = new AtomicReference<JsVariable>(null);

  /**
   * A script associated with the frame.
   */
  private Script script;

  /**
   * Constructs a call frame for the given handler using the FrameMirror data
   * from the remote JavaScript VM.
   *
   * @param frameObject frame in the VM
   * @param context in which the call frame is created
   */
  public CallFrameImpl(FrameObject frameObject, InternalContext context) {
    this.frameObject = frameObject;
    this.context = context;

    Map func = frameObject.func();
    int currentLine = frameObject.line();
    // If we stopped because of the debuggerword then we're on the next
    // line.
    // TODO(apavlov): Terry says: we need to use the [e.g. Rhino] AST to
    // decide if line is debuggerword. If so, find the next sequential line.
    // The below works for simple scripts but doesn't take into account
    // comments, etc.
    // TODO(peter.rybin): do we really need this thing? (advancing to the next line?)
    //     stopping on "debugger;" seems to be a quite natural thing.
    String srcLine = frameObject.sourceLineText();
    if (srcLine.trim().startsWith(DEBUGGER_RESERVED)) {
      currentLine++;
    }
    int scriptRef = V8ProtocolUtil.getObjectRef(frameObject.script());
    scriptId = ScriptImpl.getScriptId(context.getValueLoader().getSpecialHandleManager(), scriptRef);
    lineNumber = currentLine;
    frameFunction = V8ProtocolUtil.getFunctionName(func);
    frameId = frameObject.index();
  }

  public InternalContext getInternalContext() {
    return context;
  }

  @Override
  public List<? extends JsScope> getVariableScopes() {
    ensureScopes();
    return scopesRef.get();
  }

  @Override
  public JsVariable getReceiverVariable() throws MethodIsBlockingException {
    ensureReceiver();
    return receiverVariableRef.get();
  }

  @Override
  public JsEvaluateContext getEvaluateContext() {
    return evaluateContextImpl;
  }

  private void ensureScopes() {
    if (scopesRef.get() != null) {
      return;
    }
    scopesRef.compareAndSet(null, createScopes());
  }

  private void ensureReceiver() throws MethodIsBlockingException {
    if (receiverVariableRef.get() != null) {
      return;
    }

    PropertyReference ref = V8ProtocolUtil.extractProperty(frameObject.receiver(), PropertyNameGetter.THIS);
    if (ref == null) {
      return;
    }
    ValueLoader valueLoader = context.getValueLoader();
    ValueMirror mirror = valueLoader.getOrLoadValueFromRefs(Collections.singletonList(ref))[0];
    // This name should be string. We are making it string as a fall-back strategy.
    receiverVariableRef.compareAndSet(null, new JsVariableImpl(valueLoader, mirror, ref.getName().toString()));
  }

  @Override
  public TextStreamPosition getStatementStartPosition() {
    return textStreamPosition;
  }

  @Override
  public String getFunctionName() {
    return frameFunction;
  }

  @Override
  public Script getScript() {
    return script;
  }

  /**
   * @return this call frame's unique identifier within the V8 VM (0 is the top
   *         frame)
   */
  public int getIdentifier() {
    return frameId;
  }

  void hookUpScript(ScriptManager scriptManager) {
    Script script = scriptManager.findById(scriptId);
    if (script != null) {
      this.script = script;
    }
  }

  private List<JsScopeImpl<?>> createScopes() {
    List<ScopeRef> scopes = frameObject.scopes();
    JsScopeImpl<?>[] result = new JsScopeImpl<?>[scopes.size()];
    for (int i = 0; i < scopes.size(); i++) {
      ScopeRef scopeRef = scopes.get(i);
      result[i] = JsScopeImpl.create(JsScopeImpl.Host.create(this), scopeRef);
    }
    return Arrays.asList(result);
  }

  private final JsEvaluateContextImpl evaluateContextImpl = new JsEvaluateContextImpl() {
    @Override
    protected int getFrameIdentifier() {
      return getIdentifier();
    }
    @Override
    public InternalContext getInternalContext() {
      return context;
    }
  };

  private final TextStreamPosition textStreamPosition = new TextStreamPosition() {
    @Override public int getOffset() {
      return frameObject.position();
    }
    @Override public int getLine() {
      return lineNumber;
    }
    @Override public int getColumn() {
      return frameObject.column();
    }
  };

  /**
   * Implements restart frame operation as chain of VM calls. After the main 'restart' command
   * it either calls 'step in' request or reloads backtrace. {@link RelaySyncCallback} is used
   * to ensure final sync callback call guarantee.
   */
  public static final RestartFrameExtension RESTART_FRAME_EXTENSION = new RestartFrameExtension() {
    @Override
    public RelayOk restartFrame(CallFrame callFrame,
        final GenericCallback<Boolean> callback, SyncCallback syncCallback) {
      CallFrameImpl frameImpl = (CallFrameImpl) callFrame;
      final DebugSession debugSession = frameImpl.context.getDebugSession();

      RelaySyncCallback relaySyncCallback = new RelaySyncCallback(syncCallback);

      final RelaySyncCallback.Guard guard = relaySyncCallback.newGuard();

      RestartFrameMessage message = new RestartFrameMessage(frameImpl.frameId);
      V8CommandCallbackBase v8Callback = new V8CommandCallbackBase() {
        @Override
        public void success(CommandResponse.Success successResponse) {
          RelayOk relayOk =
              handleRestartResponse(successResponse, debugSession, callback, guard.getRelay());
          guard.discharge(relayOk);
        }

        @Override
        public void failure(String message) {
          if (callback != null) {
            callback.failure(new Exception(message));
          }
        }
      };

      try {
        return frameImpl.context.sendV8CommandAsync(message, false, v8Callback,
            guard.asSyncCallback());
      } catch (InternalContext.ContextDismissedCheckedException e) {
        throw new InvalidContextException(e);
      }
    }

    private RelayOk handleRestartResponse(CommandResponse.Success successResponse, DebugSession debugSession, GenericCallback<Boolean> callback, RelaySyncCallback relaySyncCallback) {
      InternalContext.UserContext debugContext = debugSession.getContextBuilder().getCurrentDebugContext();
      if (debugContext == null) {
        // We may have already issued 'continue' since the moment that change live command
        // was sent so the context was dropped. Ignore this case.
        return finishRestartSuccessfully(false, callback, relaySyncCallback);
      }

      if (successResponse.body().asRestartFrameBody().getResultDescription().stack_update_needs_step_in() == Boolean.TRUE) {
        return stepIn(debugContext, callback, relaySyncCallback);
      }
      else {
        return reloadStack(debugContext, callback, relaySyncCallback);
      }
    }

    private RelayOk stepIn(InternalContext.UserContext debugContext,
        final GenericCallback<Boolean> callback, final RelaySyncCallback relaySyncCallback) {
      final RelaySyncCallback.Guard guard = relaySyncCallback.newGuard();
      DebugContext.ContinueCallback continueCallback = new DebugContext.ContinueCallback() {
        @Override
        public void success() {
          RelayOk relayOk = finishRestartSuccessfully(true, callback, relaySyncCallback);
          guard.discharge(relayOk);
        }
        @Override
        public void failure(String errorMessage) {
          if (callback != null) {
            callback.failure(new Exception(errorMessage));
          }
        }
      };
      return debugContext.continueVm(DebugContext.StepAction.IN, 0,
          continueCallback, guard.asSyncCallback());
    }

    private RelayOk reloadStack(InternalContext.UserContext debugContext, final GenericCallback<Boolean> callback, final RelaySyncCallback relaySyncCallback) {
      final RelaySyncCallback.Guard guard = relaySyncCallback.newGuard();
      final ContextBuilder.ExpectingBacktraceStep backtraceStep = debugContext.createReloadBacktraceStep();
      V8CommandCallbackBase v8Callback = new V8CommandCallbackBase() {
        @Override
        public void success(CommandResponse.Success successResponse) {
          BacktraceProcessor.setFrames(successResponse, backtraceStep);
          guard.discharge(finishRestartSuccessfully(false, callback, relaySyncCallback));
        }

        @Override
        public void failure(String message) {
          if (callback != null) {
            callback.failure(new Exception(message));
          }
        }
      };

      // Command is not immediate because we are supposed to be suspended.
      return debugContext.getInternalContext().sendV8CommandAsync(new BacktraceMessage(-1, -1, true), false, v8Callback, guard.asSyncCallback());
    }

    private RelayOk finishRestartSuccessfully(boolean vmResumed,
        GenericCallback<Boolean> callback, RelaySyncCallback relaySyncCallback) {
      if (callback != null) {
        callback.success(vmResumed);
      }
      return relaySyncCallback.finish();
    }

    @Override
    public boolean canRestartFrame(CallFrame callFrame) {
      return callFrame.getScript() != null;
    }
  };

  private static final String DEBUGGER_RESERVED = "debugger";
}
