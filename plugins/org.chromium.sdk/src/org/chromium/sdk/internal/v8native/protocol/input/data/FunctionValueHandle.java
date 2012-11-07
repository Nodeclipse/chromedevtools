// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.sdk.internal.v8native.protocol.input.data;

import com.google.gson.stream.JsonReader;
import org.chromium.sdk.internal.protocolparser.*;
import org.chromium.sdk.internal.v8native.protocol.input.ScopeRef;

import java.util.List;

@JsonType
public interface FunctionValueHandle extends JsonSubtype<ObjectValueHandle> {
  @JsonOptionalField
  Long position();

  @JsonOptionalField
  Long line();

  @JsonOptionalField
  JsonReader script();

  @JsonSubtypeCondition
  boolean resolved();

  @JsonOptionalField
  String source();

  @JsonOptionalField
  String inferredName();

  @JsonOptionalField
  String name();

  @JsonOptionalField
  Long column();

  @JsonOptionalField
  Long scriptId();

  @JsonField
  List<ScopeRef> scopes();
}
