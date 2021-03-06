// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.sdk.internal.v8native.protocol.input;

import com.google.gson.stream.JsonReaderEx;
import org.chromium.protocolReader.JsonOptionalField;
import org.chromium.protocolReader.JsonType;
import org.chromium.sdk.internal.v8native.protocol.input.data.ValueHandle;

@JsonType
public interface BreakEventBody {
  @JsonOptionalField
  int[] breakpoints();

  @JsonOptionalField
  ValueHandle exception();

  @JsonOptionalField
  String sourceLineText();

  @JsonOptionalField
  boolean uncaught();

  @JsonOptionalField
  int sourceLine();

  @JsonOptionalField
  String invocationText();

  @JsonOptionalField
  JsonReaderEx script();

  @JsonOptionalField
  int sourceColumn();
}