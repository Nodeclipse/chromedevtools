// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.inputdom;

/**
 Called when shadow root is popped from the element.
 */
@org.chromium.protocolParser.JsonType
public interface ShadowRootPoppedEventData {
  /**
   Host element id.
   */
  long hostId();

  /**
   Shadow root id.
   */
  long rootId();

  public static final org.jetbrains.wip.protocol.WipEventType<org.chromium.wip.protocol.inputdom.ShadowRootPoppedEventData> TYPE
      = new org.jetbrains.wip.protocol.WipEventType<org.chromium.wip.protocol.inputdom.ShadowRootPoppedEventData>("DOMshadowRootPopped", org.chromium.wip.protocol.inputdom.ShadowRootPoppedEventData.class) {
    @Override public org.chromium.wip.protocol.inputdom.ShadowRootPoppedEventData parse(org.chromium.wip.protocol.input.GeneratedWipProtocolReader parser, com.google.gson.stream.JsonReader reader) throws java.io.IOException {
      return parser.parseDOMShadowRootPoppedEventData(reader);
    }
  };
}
