// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.sdk.internal.v8native.protocol.input.data;

import org.chromium.protocolParser.JsonOptionalField;
import org.chromium.protocolParser.JsonSubtype;
import org.chromium.protocolParser.JsonSubtypeCasting;
import org.chromium.protocolParser.JsonType;

import java.util.List;

@JsonType
public interface ObjectValueHandle extends JsonSubtype<ValueHandle> {
  List<PropertyObject> properties();

  @JsonOptionalField
  List<PropertyObject> internalProperties();

  SomeRef protoObject();
  SomeRef constructorFunction();

  @JsonOptionalField
  SomeRef primitiveValue();

  @JsonOptionalField
  SomeRef prototypeObject();

  @JsonSubtypeCasting
  FunctionValueHandle asFunction();
}