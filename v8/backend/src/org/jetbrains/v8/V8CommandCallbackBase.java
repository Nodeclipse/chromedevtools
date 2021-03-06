// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.jetbrains.v8;

import org.chromium.sdk.internal.v8native.protocol.input.CommandResponse;

/**
 * A basic implementation of {@link V8CommandCallback} that introduces
 * command success and failure handlers and dispatches the V8 response accordingly.
 */
public abstract class V8CommandCallbackBase implements V8CommandCallback {
  public abstract void success(CommandResponse.Success successResponse);

  public abstract void failure(String message);

  public void messageReceived(CommandResponse response) {
    CommandResponse.Success successResponse = response.asSuccess();
    if (successResponse == null) {
      this.failure("Remote error: " + response.asError().message());
      return;
    } else {
      success(successResponse);
    }
  }
}
