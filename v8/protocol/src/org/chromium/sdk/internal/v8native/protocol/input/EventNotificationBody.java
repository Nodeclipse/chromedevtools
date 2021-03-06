// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.sdk.internal.v8native.protocol.input;

import org.chromium.protocolReader.JsonSubtypeCasting;
import org.chromium.protocolReader.JsonType;

import java.io.IOException;

/**
 * This is empty base type for all event notification body types. The actual type
 * depends on a particular event.
 */
@JsonType
public interface EventNotificationBody {
  @JsonSubtypeCasting
  BreakEventBody asBreakEventBody() throws IOException;

  @JsonSubtypeCasting
  AfterCompileBody asAfterCompileBody() throws IOException;

  @JsonSubtypeCasting
  ScriptCollectedBody asScriptCollectedBody() throws IOException;
}
