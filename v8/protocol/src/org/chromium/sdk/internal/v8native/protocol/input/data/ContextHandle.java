// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.sdk.internal.v8native.protocol.input.data;

import com.google.gson.stream.JsonReaderEx;
import org.chromium.protocolReader.JsonOptionalField;
import org.chromium.protocolReader.JsonType;

@JsonType
public interface ContextHandle {
  /**
   * Any value, provided by V8 embedding application. For Chrome it may be {@link ContextData}
   * as well as its stringified form (as "type,in").
   */
  @JsonOptionalField
  JsonReaderEx data();
}
