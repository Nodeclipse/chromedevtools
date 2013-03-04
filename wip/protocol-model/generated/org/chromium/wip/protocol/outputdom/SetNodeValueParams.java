// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.outputdom;

/**
Sets node value for a node with given id.
 */
public class SetNodeValueParams extends org.jetbrains.wip.protocol.WipParams {
  /**
   @param nodeId Id of the node to set value for.
   @param value New node's value.
   */
  public SetNodeValueParams(long nodeId, String value) {
    //this.put("nodeId", nodeId);
    //this.put("value", value);
  }

  public static final String METHOD_NAME = org.jetbrains.wip.protocol.BasicConstants.Domain.DOM + ".setNodeValue";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
