// Generated source
package org.jetbrains.wip.protocol.network;

/**
 * WebSocket response data.
 */
@org.chromium.protocolReader.JsonType
public interface WebSocketResponseValue {
  /**
   * HTTP response status code.
   */
  double status();
  /**
   * HTTP response status text.
   */
  String statusText();
  /**
   * HTTP response headers.
   */
  org.jetbrains.wip.protocol.network.HeadersValue headers();

}