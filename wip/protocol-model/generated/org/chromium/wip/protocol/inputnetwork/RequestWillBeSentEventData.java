// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.inputnetwork;

/**
 Fired when page is about to send HTTP request.
 */
@org.chromium.protocolParser.JsonType
public interface RequestWillBeSentEventData {
  /**
   Request identifier.
   */
  String requestId();

  /**
   Frame identifier.
   */
  String frameId();

  /**
   Loader identifier.
   */
  String loaderId();

  /**
   URL of the document this request is loaded for.
   */
  String documentURL();

  /**
   Request data.
   */
  org.chromium.wip.protocol.inputnetwork.RequestValue request();

  /**
   Timestamp.
   */
  Number timestamp();

  /**
   Request initiator.
   */
  org.chromium.wip.protocol.inputnetwork.InitiatorValue initiator();

  /**
   Redirect response data.
   */
  @org.chromium.protocolParser.JsonOptionalField
  org.chromium.wip.protocol.inputnetwork.ResponseValue redirectResponse();

  public static final org.jetbrains.wip.protocol.WipEventType<org.chromium.wip.protocol.inputnetwork.RequestWillBeSentEventData> TYPE
      = new org.jetbrains.wip.protocol.WipEventType<org.chromium.wip.protocol.inputnetwork.RequestWillBeSentEventData>("NetworkrequestWillBeSent", org.chromium.wip.protocol.inputnetwork.RequestWillBeSentEventData.class) {
    @Override public org.chromium.wip.protocol.inputnetwork.RequestWillBeSentEventData parse(org.chromium.wip.protocol.input.WipGeneratedParserRoot parser, com.google.gson.stream.JsonReader reader) {
      return parser.parseNetworkRequestWillBeSentEventData(reader);
    }
  };
}
