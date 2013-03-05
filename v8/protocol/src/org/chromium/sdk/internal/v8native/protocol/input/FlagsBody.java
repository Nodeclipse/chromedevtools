// Copyright (c) 2010 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.sdk.internal.v8native.protocol.input;

import java.util.List;

import org.chromium.protocolParser.JsonSubtype;
import org.chromium.protocolParser.JsonType;

@JsonType
public interface FlagsBody extends JsonSubtype<CommandResponseBody> {
  List<FlagInfo> flags();

  @JsonType
  interface FlagInfo {
    String name();
    Object value();
  }
}