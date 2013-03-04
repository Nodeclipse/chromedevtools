// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.inputnetwork;

/**
 Fired when data chunk was received over the network.
 */
@org.chromium.protocolParser.JsonType
public interface DataReceivedEventData {
  /**
   Request identifier.
   */
  String/*See org.chromium.wip.protocol.commonnetwork.RequestIdTypedef*/ requestId();

  /**
   Timestamp.
   */
  Number/*See org.chromium.wip.protocol.commonnetwork.TimestampTypedef*/ timestamp();

  /**
   Data chunk length.
   */
  long dataLength();

  /**
   Actual bytes received (might be less than dataLength for compressed encodings).
   */
  long encodedDataLength();

  public static final org.jetbrains.wip.protocol.WipEventType<org.chromium.wip.protocol.inputnetwork.DataReceivedEventData> TYPE
      = new org.jetbrains.wip.protocol.WipEventType<org.chromium.wip.protocol.inputnetwork.DataReceivedEventData>("NetworkdataReceived", org.chromium.wip.protocol.inputnetwork.DataReceivedEventData.class) {
    @Override public org.chromium.wip.protocol.inputnetwork.DataReceivedEventData parse(org.chromium.wip.protocol.input.WipGeneratedParserRoot parser, com.google.gson.stream.JsonReader reader) {
      return parser.parseNetworkDataReceivedEventData(reader);
    }
  };
}
