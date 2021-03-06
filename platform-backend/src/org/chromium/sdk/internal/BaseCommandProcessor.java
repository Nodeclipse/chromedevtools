// Copyright (c) 2010 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.sdk.internal;

import org.chromium.sdk.RelayOk;
import org.chromium.sdk.SyncCallback;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides basic command processor functionality: sends/receives commands/events and
 * supports callbacks for commands. It also supports status reporting for UI.
 * All operations such as sending/receiving/parsing are implemented by a {@link Handler}.
 *
 * @param <OUTGOING> type of outgoing message
 * @param <INCOMING> type of incoming message
 * @param <INCOMING_WITH_SEQ> type of incomming message that is a command (has sequence number)
 */
public class BaseCommandProcessor<OUTGOING, INCOMING, INCOMING_WITH_SEQ> {
  private static final Logger LOGGER = Logger.getLogger(BaseCommandProcessor.class.getName());

  public interface Handler<OUTGOING, INCOMING, INCOMING_WITH_SEQ> {
    int getUpdatedSequence(OUTGOING message);
    String getMethodName(OUTGOING message);
    void send(OUTGOING message, boolean isImmediate) throws IOException;
    INCOMING_WITH_SEQ readIfHasSequence(INCOMING incoming);
    int getSequence(INCOMING_WITH_SEQ incomingWithSeq);
    void acceptNonSequence(INCOMING incoming);
    void reportVmStatus(String currentRequest, int numberOfEnqueued);
  }

  public interface Callback<INCOMING_WITH_SEQ> {
    void messageReceived(INCOMING_WITH_SEQ response);
    void failure(String message);
  }

  private final CloseableMap<Integer, CallbackEntry<INCOMING_WITH_SEQ>> callbackMap = CloseableMap.newLinkedMap();
  private final Handler<OUTGOING, INCOMING, INCOMING_WITH_SEQ> handler;

  public BaseCommandProcessor(Handler<OUTGOING, INCOMING, INCOMING_WITH_SEQ> handler) {
    this.handler = handler;
  }

  public RelayOk send(OUTGOING message, boolean isImmediate, @Nullable Callback<? super INCOMING_WITH_SEQ> callback, @Nullable SyncCallback syncCallback) {
    int sequence = handler.getUpdatedSequence(message);
    boolean callbackAdded;
    if (callback != null || syncCallback != null) {
      String commandName = handler.getMethodName(message);
      try {
        callbackMap.put(sequence, new CallbackEntry<INCOMING_WITH_SEQ>(callback, syncCallback, commandName));
      }
      catch (IllegalStateException e) {
        throw new IllegalStateException("Connection is closed", e);
      }
      callbackAdded = true;
      reportVmStatus();
    }
    else {
      callbackAdded = false;
    }
    boolean sent = false;
    try {
      handler.send(message, isImmediate);
      sent = true;
    }
    catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Failed to send", e);
    }
    finally {
      if (!sent && callbackAdded) {
        callbackMap.remove(sequence);
      }
    }
    return WE_SENT_IT_RELAY_OK;
  }

  public void processIncoming(INCOMING incomingParsed) {
    final INCOMING_WITH_SEQ commandResponse = handler.readIfHasSequence(incomingParsed);
    if (commandResponse == null) {
      handler.acceptNonSequence(incomingParsed);
      return;
    }

    int key = handler.getSequence(commandResponse);
    CallbackEntry<INCOMING_WITH_SEQ> callbackEntry = callbackMap.removeIfContains(key);
    if (callbackEntry == null) {
      return;
    }

    LOGGER.log(Level.FINE, "Request-response roundtrip: {0}ms", getCurrentMillis() - callbackEntry.commitMillis);
    reportVmStatus();

    CallbackCaller<Callback<? super INCOMING_WITH_SEQ>> caller = new CallbackCaller<Callback<? super INCOMING_WITH_SEQ>>() {
      @Override
      void call(Callback<? super INCOMING_WITH_SEQ> handlerCallback) {
        handlerCallback.messageReceived(commandResponse);
      }
    };
    try {
      callThemBack(callbackEntry, caller, key);
    }
    catch (RuntimeException e) {
      LOGGER.log(Level.SEVERE, "Failed to dispatch response to callback", e);
    }
  }

  public void processEos() {
    // We should call them in the order they have been submitted.
    Collection<CallbackEntry<INCOMING_WITH_SEQ>> entries = callbackMap.close().values();
    for (CallbackEntry<INCOMING_WITH_SEQ> entry : entries) {
      try {
        callThemBack(entry, failureCaller, -1);
      }
      catch (RuntimeException e) {
        LOGGER.log(Level.SEVERE, "Failed to dispatch response to callback", e);
      }
    }
  }

  private void callThemBack(CallbackEntry<INCOMING_WITH_SEQ> callbackEntry,
                            CallbackCaller<? super Callback<? super INCOMING_WITH_SEQ>> callbackCaller,
                            int requestSeq) {
    RuntimeException callbackException = null;
    try {
      if (callbackEntry.callback != null) {
        LOGGER.log(Level.FINE, "Notified debugger command callback, request_seq={0}", requestSeq);
        callbackCaller.call(callbackEntry.callback);
      }
    }
    catch (RuntimeException e) {
      callbackException = e;
      throw e;
    }
    finally {
      if (callbackEntry.syncCallback != null) {
        callbackEntry.syncCallback.callbackDone(callbackException);
      }
    }
  }

  private static abstract class CallbackCaller<CALLBACK> {
    abstract void call(CALLBACK handlerCallback);
  }

  private final CallbackCaller<Callback<?>> failureCaller = new CallbackCaller<Callback<?>>() {
    @Override
    void call(Callback<?> handlerCallback) {
      handlerCallback.failure("Connection closed");
    }
  };

  private static class CallbackEntry<INCOMING_WITH_SEQ> {
    final Callback<? super INCOMING_WITH_SEQ> callback;
    final SyncCallback syncCallback;
    final long commitMillis;
    final String requestName;

    CallbackEntry(Callback<? super INCOMING_WITH_SEQ> callback, SyncCallback syncCallback, String requestName) {
      this.callback = callback;
      commitMillis = getCurrentMillis();
      this.syncCallback = syncCallback;
      this.requestName = requestName;
    }
  }

  /**
   * @return milliseconds since the epoch
   */
  private static long getCurrentMillis() {
    return System.currentTimeMillis();
  }

  private final Object vmStatusReportMonitor = new Object();
  private void reportVmStatus() {
    // We synchronize, because one thread may be delivering obsolete message while a more
    // recent message has already been delivered by other thread.
    synchronized (vmStatusReportMonitor) {
      int size = callbackMap.size();
      CallbackEntry<?> firstEntry = callbackMap.peekFirst();
      // Those 2 variables above might be not in synch, so for a brief moment user may see
      // a wrong message (when size == 0 and firstEntry is null). This is OK.
      if (firstEntry == null) {
        handler.reportVmStatus(null, 0);
      }
      else {
        handler.reportVmStatus(firstEntry.requestName, size - 1);
      }
    }
  }

  private static final RelayOk WE_SENT_IT_RELAY_OK = new RelayOk() {};
}
