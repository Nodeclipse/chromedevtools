// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.jetbrains.wip.protocol;

import org.chromium.wip.protocol.input.WipGeneratedParserRoot;

/**
 * A base class for all method parameter classes that implies non-empty responses.
 * @param <R> a type of the corresponding response
 */
public abstract class WipParamsWithResponse<R> extends WipParams {
  public abstract R parseResponse(WipCommandResponse.Data success, WipGeneratedParserRoot parser) throws java.io.IOException;
}