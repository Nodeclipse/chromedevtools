// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.inputnetwork;

/**
 WebSocket frame data.
 */
@org.chromium.sdk.internal.protocolparser.JsonType
public interface WebSocketFrameValue {
  /**
   WebSocket frame opcode.
   */
  Number opcode();

  /**
   WebSocke frame mask.
   */
  boolean mask();

  /**
   WebSocke frame payload data.
   */
  String payloadData();

}