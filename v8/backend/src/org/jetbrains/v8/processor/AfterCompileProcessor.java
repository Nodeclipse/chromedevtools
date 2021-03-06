// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.jetbrains.v8.processor;

import org.chromium.sdk.Script;
import org.jetbrains.v8.DebugSession;
import org.jetbrains.v8.V8CommandCallbackBase;
import org.jetbrains.v8.V8ContextFilter;
import org.jetbrains.v8.V8Helper;
import org.jetbrains.v8.protocol.V8ProtocolUtil;
import org.chromium.sdk.internal.v8native.protocol.input.AfterCompileBody;
import org.chromium.sdk.internal.v8native.protocol.input.CommandResponse;
import org.chromium.sdk.internal.v8native.protocol.input.EventNotification;
import org.chromium.sdk.internal.v8native.protocol.input.data.ScriptHandle;
import org.chromium.sdk.internal.v8native.protocol.output.ScriptsMessage;

import java.io.IOException;
import java.util.List;

/**
 * Listens for scripts sent in the "afterCompile" events and requests their
 * sources.
 */
public class AfterCompileProcessor extends V8EventProcessor {

  public AfterCompileProcessor(DebugSession debugSession) {
    super(debugSession);
  }

  @Override
  public void messageReceived(EventNotification eventMessage) {
    final DebugSession debugSession = getDebugSession();
    ScriptHandle script = getScriptToLoad(eventMessage,
        debugSession.getScriptManager().getContextFilter());
    if (script == null) {
      return;
    }
    debugSession.sendMessage(
      new ScriptsMessage(new long[]{script.id()}, true),
      new V8CommandCallbackBase() {
        @Override
        public void success(CommandResponse.Success successResponse) {
          List<ScriptHandle> body = successResponse.body().asScripts();
          // body is an array of scripts
          if (body.size() == 0) {
            return; // The script did not arrive (bad id?)
          }
          debugSession.getScriptManager().addScript(body.get(0), successResponse.refs());
        }

        @Override
        public void failure(String message) {
          // The script is now missing.
        }
      },
      null);
  }

  private static ScriptHandle getScriptToLoad(EventNotification eventResponse,
      V8ContextFilter contextFilter) {
    AfterCompileBody body;
    try {
      body = eventResponse.body().asAfterCompileBody();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    ScriptHandle script = body.script();
    if (V8Helper.JAVASCRIPT_VOID.equals(script.sourceStart()) ||
        script.context() == null ||
        V8ProtocolUtil.getScriptType(script.scriptType()) ==
            Script.Type.NATIVE) {
      return null;
    }
    return V8ProtocolUtil.validScript(script, eventResponse.refs(), contextFilter);
  }
}
