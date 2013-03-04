// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.inputnetwork;

/**
 Fired upon WebSocket creation.
 */
@org.chromium.protocolParser.JsonType
public interface WebSocketCreatedEventData {
  /**
   Request identifier.
   */
  String/*See org.chromium.wip.protocol.commonnetwork.RequestIdTypedef*/ requestId();

  /**
   WebSocket request URL.
   */
  String url();

  public static final org.jetbrains.wip.protocol.WipEventType<org.chromium.wip.protocol.inputnetwork.WebSocketCreatedEventData> TYPE
      = new org.jetbrains.wip.protocol.WipEventType<org.chromium.wip.protocol.inputnetwork.WebSocketCreatedEventData>("NetworkwebSocketCreated", org.chromium.wip.protocol.inputnetwork.WebSocketCreatedEventData.class) {
    @Override public org.chromium.wip.protocol.inputnetwork.WebSocketCreatedEventData parse(org.chromium.wip.protocol.input.WipGeneratedParserRoot parser, com.google.gson.stream.JsonReader reader) {
      return parser.parseNetworkWebSocketCreatedEventData(reader);
    }
  };
}
