// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.jetbrains.v8;

import org.chromium.sdk.ConnectionLogger;
import org.chromium.sdk.DebugEventListener;
import org.chromium.sdk.StandaloneVm;
import org.chromium.sdk.UnsupportedVersionException;
import org.chromium.sdk.internal.BrowserFactoryImpl;
import org.chromium.sdk.internal.transport.Connection;
import org.chromium.sdk.internal.transport.Connection.NetListener;
import org.chromium.sdk.internal.transport.Message;
import org.chromium.sdk.internal.transport.SocketConnection;
import org.chromium.sdk.internal.v8native.protocol.input.data.ContextHandle;
import org.chromium.sdk.util.MethodIsBlockingException;
import org.jetbrains.jsonProtocol.JsonReaders;
import org.jetbrains.jsonProtocol.Request;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Implementation of {@code StandaloneVm}. Currently knows nothing about
 * contexts, so all existing V8 contexts are presented mixed together.
 */
public class StandaloneVmImpl extends JavascriptVmImpl implements StandaloneVm {
  private static final int WAIT_FOR_HANDSHAKE_TIMEOUT_MS = 3000;

  private static final V8ContextFilter CONTEXT_FILTER = new V8ContextFilter() {
    public boolean isContextOurs(ContextHandle contextHandle) {
      // We do not check context in standalone V8 mode.
      return true;
    }
  };

  private final SocketConnection connection;
  private final StandaloneV8Handshaker handshaker;

  private final DebugSession debugSession;

  private DebugEventListener debugEventListener = null;
  private volatile ConnectionState connectionState = ConnectionState.INIT;

  private volatile Exception disconnectReason = null;
  private volatile StandaloneV8Handshaker.RemoteInfo savedRemoteInfo = NULL_REMOTE_INFO;

  private final Object disconnectMonitor = new Object();

  public static StandaloneVm createStandalone(SocketAddress socketAddress, ConnectionLogger connectionLogger) {
    StandaloneV8Handshaker handshaker = new StandaloneV8Handshaker();
    return new StandaloneVmImpl(new SocketConnection(socketAddress, BrowserFactoryImpl.getTimeout(), connectionLogger, handshaker), handshaker);
  }

  public StandaloneVmImpl(SocketConnection connection, StandaloneV8Handshaker handshaker) {
    this.connection = connection;
    this.handshaker = handshaker;
    V8CommandOutputImpl v8CommandOutput = new V8CommandOutputImpl(connection);
    DebugSessionManager sessionManager = new DebugSessionManager() {
      public DebugEventListener getDebugEventListener() {
        return debugEventListener;
      }

      public void onDebuggerDetached() {
        // Never called for standalone.
      }
    };
    debugSession = new DebugSession(sessionManager, CONTEXT_FILTER, v8CommandOutput, this);
  }

  public void attach(DebugEventListener listener) throws IOException, UnsupportedVersionException, MethodIsBlockingException {
    Exception errorCause = null;
    try {
      attachImpl(listener);
    }
    catch (IOException e) {
      errorCause = e;
      throw e;
    }
    catch (UnsupportedVersionException e) {
      errorCause = e;
      throw e;
    }
    finally {
      if (errorCause != null) {
        disconnectReason = errorCause;
        connectionState = ConnectionState.DETACHED;
        connection.close();
      }
    }
  }

  private void attachImpl(DebugEventListener listener) throws IOException, UnsupportedVersionException, MethodIsBlockingException {
    connectionState = ConnectionState.CONNECTING;

    NetListener netListener = new NetListener() {
      public void connectionClosed() {
      }

      public void eosReceived() {
        debugSession.getCommandProcessor().processEos();
        onDebuggerDetachedImpl(null);
      }

      public void messageReceived(Message message) {
        debugSession.getCommandProcessor().processIncomingJson(JsonReaders.createReader(message.getContent()));
      }
    };
    connection.setNetListener(netListener);
    connection.start();
    connectionState = ConnectionState.EXPECTING_HANDSHAKE;

    StandaloneV8Handshaker.RemoteInfo remoteInfo;
    try {
      remoteInfo = handshaker.getRemoteInfo().get(WAIT_FOR_HANDSHAKE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
    }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    catch (ExecutionException e) {
      throw newIOException("Failed to get version", e);
    }
    catch (TimeoutException e) {
      throw newIOException("Timed out waiting for handshake", e);
    }

    String versionString = remoteInfo.getProtocolVersion();
    // TODO(peter.rybin): check version here
    if (versionString == null) {
      throw new UnsupportedVersionException(null, null);
    }

    savedRemoteInfo = remoteInfo;
    debugEventListener = listener;
    debugSession.startCommunication();
    connectionState = ConnectionState.CONNECTED;
  }

  public boolean detach() {
    boolean res = onDebuggerDetachedImpl(null);
    if (!res) {
      return false;
    }
    connection.close();
    return true;
  }

  public boolean isAttached() {
    return connectionState == ConnectionState.CONNECTED;
  }

  private boolean onDebuggerDetachedImpl(Exception cause) {
    synchronized (disconnectMonitor) {
      if (!isAttached()) {
        // We've already been notified.
        return false;
      }
      connectionState = ConnectionState.DETACHED;
      disconnectReason = cause;
    }
    if (debugEventListener != null) {
      debugEventListener.disconnected();
    }
    return true;
  }

  @Override
  protected DebugSession getDebugSession() {
    return debugSession;
  }

  /**
   * @return name of embedding application as it wished to name itself; might be null
   */
  public String getEmbedderName() {
    return savedRemoteInfo.getEmbeddingHostName();
  }

  /**
   * @return version of V8 implementation, format is unspecified; not null
   */
  public String getVmVersion() {
    return savedRemoteInfo.getV8VmVersion();
  }

  public String getDisconnectReason() {
    // Save volatile field in local variable.
    Exception cause = disconnectReason;
    if (cause == null) {
      return null;
    }
    return cause.getMessage();
  }

  private final static StandaloneV8Handshaker.RemoteInfo NULL_REMOTE_INFO =
    new StandaloneV8Handshaker.RemoteInfo() {
      public String getEmbeddingHostName() {
        return null;
      }

      public String getProtocolVersion() {
        return null;
      }

      public String getV8VmVersion() {
        return null;
      }
    };

  private enum ConnectionState {
    INIT,
    CONNECTING,
    EXPECTING_HANDSHAKE,
    CONNECTED,
    DETACHED
  }

  private static class V8CommandOutputImpl implements V8CommandOutput {
    private final Connection outputConnection;

    V8CommandOutputImpl(Connection outputConnection) {
      this.outputConnection = outputConnection;
    }

    public void send(Request debuggerMessage, boolean immediate) {
      outputConnection.send(new Message(Collections.<String, String>emptyMap(), debuggerMessage.toJson()));
    }

    public void runInDispatchThread(Runnable callback) {
      outputConnection.runInDispatchThread(callback);
    }
  }
}
