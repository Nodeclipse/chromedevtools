// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.sdk.internal.v8native.protocol.input;

import org.chromium.protocolReader.JsonOptionalField;
import org.chromium.protocolReader.JsonType;

import java.util.List;

@JsonType
public interface BacktraceCommandBody {
  @JsonOptionalField
  List<FrameObject> frames();

  @JsonOptionalField
  int fromFrame();

  @JsonOptionalField
  int toFrame();

  int totalFrames();
}
