// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.jetbrains.v8;

import org.chromium.sdk.*;
import org.chromium.sdk.JavascriptVm.ScriptsCallback;
import org.chromium.sdk.JavascriptVm.SuspendCallback;
import org.jetbrains.v8.processor.BreakpointProcessor;
import org.chromium.sdk.internal.v8native.protocol.input.CommandResponse;
import org.chromium.sdk.internal.v8native.protocol.output.SuspendMessage;
import org.chromium.sdk.util.AsyncFuture;
import org.chromium.sdk.util.AsyncFuture.Callback;
import org.chromium.sdk.util.AsyncFutureRef;
import org.chromium.sdk.util.MethodIsBlockingException;
import org.chromium.sdk.util.RelaySyncCallback;
import org.jetbrains.v8.protocol.V8Request;
import org.jetbrains.v8.protocol.VersionResult;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that holds and administers main parts of debug protocol implementation.
 */
public class DebugSession {
  private static final Logger LOGGER = Logger.getLogger(DebugSession.class.getName());

  /** The script manager for the associated tab. */
  private final ScriptManager scriptManager;

  private final V8CommandProcessor v8CommandProcessor;

  private final ContextBuilder contextBuilder;

  /** Our manager. */
  private DebugSessionManager sessionManager;

  /** Context owns breakpoint manager. */
  private final BreakpointManager breakpointManager;

  private final ScriptManagerProxy scriptManagerProxy = new ScriptManagerProxy(this);

  private final JavascriptVm javascriptVm;

  private volatile Version vmVersion = null;

  public DebugSession(DebugSessionManager sessionManager, V8ContextFilter contextFilter,
      V8CommandOutput v8CommandOutput, JavascriptVm javascriptVm) {
    scriptManager = new ScriptManager(contextFilter, this);
    this.sessionManager = sessionManager;
    this.javascriptVm = javascriptVm;
    breakpointManager = new BreakpointManager(this);

    v8CommandProcessor = new V8CommandProcessor(v8CommandOutput, new DefaultResponseHandler(this), this);
    contextBuilder = new ContextBuilder(this);
  }

  public ScriptManager getScriptManager() {
    return scriptManager;
  }

  public V8CommandProcessor getCommandProcessor() {
    return v8CommandProcessor;
  }

  public DebugSessionManager getSessionManager() {
    return sessionManager;
  }

  public void onDebuggerDetached() {
    getSessionManager().onDebuggerDetached();
    getScriptManager().reset();
    contextBuilder.forceCancelContext();
  }

  /**
   * Sends V8 command messages, but only those which doesn't depend on context.
   * Use {@code InternalContext} if you need to send context-specific commands.
   * @return
   */
  public RelayOk sendMessage(V8Request message, boolean isImmediate, V8CommandCallback commandCallback, SyncCallback syncCallback) {
    return v8CommandProcessor.sendV8CommandAsync(message, isImmediate, commandCallback, syncCallback);
  }

  //public RelayOk sendMessage(V8Request message, boolean isImmediate, V8CommandCallback commandCallback, SyncCallback syncCallback) {
  //  return v8CommandProcessor.sendV8CommandAsync(message, isImmediate, commandCallback, syncCallback);
  //}

  public RelayOk sendMessage(V8Request message, V8CommandCallback commandCallback, SyncCallback syncCallback) {
    return sendMessage(message, true, commandCallback, syncCallback);
  }

  JavascriptVm getJavascriptVm() {
    return javascriptVm;
  }

  public Version getVmVersion() {
    return vmVersion;
  }

  /**
   * @return the DebugEventListener associated with this context
   */
  public DebugEventListener getDebugEventListener() {
    return getSessionManager().getDebugEventListener();
  }

  public BreakpointManager getBreakpointManager() {
    return breakpointManager;
  }

  public ScriptManagerProxy getScriptManagerProxy() {
    return scriptManagerProxy;
  }

  public ContextBuilder getContextBuilder() {
    return contextBuilder;
  }

  /**
   * Drops current context and creates a new one. This is useful if context is known to have changed
   * (e.g. experimental feature LiveEdit may change current stack while execution is suspended).
   * The method is asynchronous and returns immediately.
   * Does nothing if currently there is no active context. Otherwise dismisses current context,
   * invokes {@link DebugEventListener#resumed()} and initiates downloading stack frame descriptions
   * and building new context. When the context is built,
   * calls {@link DebugEventListener#suspended(DebugContext)}.
   * <p>
   * Must be called from Dispatch Thread.
   * @return true if context has been actually dropped.
   */
  public boolean recreateCurrentContext() {
    ContextBuilder.ExpectingBacktraceStep step = contextBuilder.startRebuildCurrentContext();
    if (step == null) {
      return false;
    }
    BreakpointProcessor.processNextStep(step);
    return true;
  }

  public void suspend(final SuspendCallback suspendCallback) {
    V8CommandCallback v8Callback = new V8CommandCallbackBase() {
      @Override
      public void failure(String message) {
        if (suspendCallback != null) {
          suspendCallback.failure(new Exception(message));
        }
      }
      @Override
      public void success(CommandResponse.Success successResponse) {
        if (suspendCallback != null) {
          suspendCallback.success();
        }

        ContextBuilder.ExpectingBreakEventStep step1 = contextBuilder.buildNewContextWhenIdle();
        if (step1 == null) {
          return;
        }
        ContextBuilder.ExpectingBacktraceStep step2 = step1.setContextState(Collections.<Breakpoint>emptyList(), null);
        BreakpointProcessor.processNextStep(step2);
      }
    };
    sendMessage(new SuspendMessage(), v8Callback, null);
  }

  /**
   * A proxy to script manager that makes sure that all scripts have been pre-loaded from remote.
   * This is done only once per debug session.
   * TODO: consider loading all scripts synchronously on session start.
   */
  public static class ScriptManagerProxy {
    private final DebugSession debugSession;
    private final AsyncFutureRef<Void> scriptsLoadedFuture = new AsyncFutureRef<Void>();

    ScriptManagerProxy(DebugSession debugSession) {
      this.debugSession = debugSession;
    }

    public RelayOk getAllScripts(final ScriptsCallback callback, SyncCallback syncCallback) {
      if (!scriptsLoadedFuture.isInitialized()) {
        scriptsLoadedFuture.initializeRunning(new ScriptsRequester());
      }

      // Operation is multi-step, so make sure that syncCallback won't be left uncalled.
      RelaySyncCallback relay = new RelaySyncCallback(syncCallback);
      final RelaySyncCallback.Guard guard = relay.newGuard();

      Callback<Void> futureCallback = new Callback<Void>() {
        @Override public void done(Void res) {
          if (callback != null) {
            RelayOk relayOk = getAllScriptsAsync(callback, guard.getRelay());
            guard.discharge(relayOk);
          }
        }
      };

      return scriptsLoadedFuture.getAsync(futureCallback, guard.asSyncCallback());
    }

    private RelayOk getAllScriptsAsync(final ScriptsCallback callback, RelaySyncCallback relay) {
      // We should call the callback from Dispatch thread (so that the whole collection
      // kept fresh during the call-back).
      return debugSession.getCommandProcessor().runInDispatchThread(
          new Runnable() {
            @Override
            public void run() {
              callback.success(debugSession.getScriptManager().allScripts());
            }
          },
          relay.getUserSyncCallback());
    }

    private class ScriptsRequester implements AsyncFuture.Operation<Void> {
      @Override
      public RelayOk start(final Callback<Void> requestCallback,
          SyncCallback syncCallback) {
        V8Helper.ScriptLoadCallback scriptLoadCallback = new V8Helper.ScriptLoadCallback() {
          @Override
          public void success() {
            requestCallback.done(null);
          }

          @Override
          public void failure(String message) {
            LOGGER.log(Level.SEVERE, null, new Exception("Failed to load scripts from remote: " + message));
            requestCallback.done(null);
          }
        };
        return V8Helper.reloadAllScriptsAsync(debugSession, scriptLoadCallback, syncCallback);
      }
    }
  }

  /**
   * Checks version of V8 and check if it in running state.
   */
  public void startCommunication() throws MethodIsBlockingException {
    V8CommandCallbackWithResponse<VersionResult, Void> callback = new V8CommandCallbackWithResponse<VersionResult, Void>() {
      @Override
      public Void success(VersionResult result, CommandResponse.Success response) {
        String versionString = result.V8Version();
        vmVersion = versionString == null ? null : Version.parseString(versionString);
        if (V8VersionFeatures.isRunningAccurate(vmVersion) && !response.running()) {
          ContextBuilder.ExpectingBreakEventStep step1 = contextBuilder.buildNewContextWhenIdle();
          // If step is not null -- we are already in process of building a context.
          if (step1 != null) {
            BreakpointProcessor.processNextStep(step1.setContextState(Collections.<Breakpoint>emptyList(), null));
          }
        }
        return null;
      }
    };
    V8Helper.callV8Sync(v8CommandProcessor, new org.jetbrains.v8.protocol.Version(), callback);
  }

  public RelayOk sendLoopbackMessage(Runnable callback, SyncCallback syncCallback) {
    return v8CommandProcessor.runInDispatchThread(callback, syncCallback);
  }

  public static void maybeRethrowContextException(InternalContext.ContextDismissedCheckedException e) {
    // TODO(peter.rybin): make some kind of option out of this
    boolean strictPolicy = true;
    if (strictPolicy) {
      throw new InvalidContextException(e);
    }
  }
}
