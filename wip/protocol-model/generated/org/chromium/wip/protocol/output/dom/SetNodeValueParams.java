// Generated source
package org.chromium.wip.protocol.output.dom;

/**
 * Sets node value for a node with given id.
 */
public class SetNodeValueParams extends org.jetbrains.wip.protocol.WipParams {
  /**
   * @param nodeId Id of the node to set value for.
   * @param value New node's value.
   */
  public SetNodeValueParams(int nodeId, String value) {
    put("nodeId", nodeId);
    put("value", value);
  }
  public static final String METHOD_NAME = org.jetbrains.wip.protocol.BasicConstants.Domain.DOM + ".setNodeValue";

  @Override
  public String getCommand() {
    return METHOD_NAME;
  }
}