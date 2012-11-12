// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.sdk.internal.protocolparser.dynamicimpl;

import org.chromium.sdk.internal.protocolparser.JsonProtocolParseException;
import org.chromium.sdk.internal.protocolparser.JsonSubtypeCondition;
import org.chromium.sdk.internal.protocolparser.dynamicimpl.JavaCodeGenerator.MethodScope;

/**
 * A parser that accepts value of JSON field and outputs value in another form (e.g. string
 * is converted to enum constant) to serve field getters in JsonType interfaces.
 * The parser is called "quick" because it is supposed to employ only fast conversions.
 * The quick parser should be suitable for subtype conditions
 * (see {@link JsonSubtypeCondition} etc), because they should not take long to evaluate.
 */
abstract class QuickParser<T> extends ValueParser<T> {
  QuickParser(boolean nullable) {
    super(nullable);
  }

  /**
   * Parses input value and returns output that doesn't need any post-processing
   * by {@link FieldLoadedFinisher} (see {@link ValueParser}).
   */
  public abstract T parseValueQuick(Object value) throws JsonProtocolParseException;

  @Override
  public QuickParser<T> asQuickParser() {
    return this;
  }

  @Override
  public FieldLoadedFinisher getValueFinisher() {
    return null;
  }

  @Override
  public ObjectValueParser<?> asJsonTypeParser() {
    return null;
  }

  abstract void writeParseQuickCode(MethodScope scope, String valueRef, String resultRef);
}
