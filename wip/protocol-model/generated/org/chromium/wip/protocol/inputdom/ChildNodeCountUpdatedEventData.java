// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.inputdom;

/**
 Fired when <code>Container</code>'s child node count has changed.
 */
@org.chromium.protocolParser.JsonType
public interface ChildNodeCountUpdatedEventData {
  /**
   Id of the node that has changed.
   */
  long/*See org.chromium.wip.protocol.commondom.NodeIdTypedef*/ nodeId();

  /**
   New node count.
   */
  long childNodeCount();

  public static final org.jetbrains.wip.protocol.WipEventType<org.chromium.wip.protocol.inputdom.ChildNodeCountUpdatedEventData> TYPE
      = new org.jetbrains.wip.protocol.WipEventType<org.chromium.wip.protocol.inputdom.ChildNodeCountUpdatedEventData>("DOMchildNodeCountUpdated", org.chromium.wip.protocol.inputdom.ChildNodeCountUpdatedEventData.class) {
    @Override public org.chromium.wip.protocol.inputdom.ChildNodeCountUpdatedEventData parse(org.chromium.wip.protocol.input.WipGeneratedParserRoot parser, com.google.gson.stream.JsonReader reader) {
      return parser.parseDOMChildNodeCountUpdatedEventData(reader);
    }
  };
}
