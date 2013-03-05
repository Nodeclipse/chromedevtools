// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.jetbrains.v8.protocolParser;

import org.chromium.protocolparser.DynamicParserImpl;
import org.chromium.protocolparser.ParserGeneratorBase;
import org.chromium.sdk.internal.v8native.protocol.LiveEditResult;

/**
 * A main class that generates V8 protocol static parser implementation.
 */
public class LiveEditParserGenerator extends ParserGeneratorBase {
  public static void main(String[] args) {
    mainImpl(args, createConfiguration());
  }

  public static ParserGeneratorBase.GenerateConfiguration createConfiguration() {
    DynamicParserImpl<LiveEditProtocolParser> result = new DynamicParserImpl<LiveEditProtocolParser>(true, LiveEditProtocolParser.class,
                                                                                                     new Class[]{LiveEditResult.class});
    return new GenerateConfiguration("org.chromium.v8.liveeditProtocol", "LiveEditProtocolReader", result);
  }
}