// Generated source
package org.chromium.wip.protocol.output.dom;

/**
 * Removes node with given id.
 */
public class RemoveNodeParams extends org.jetbrains.wip.protocol.WipParams {
  /**
   * @param nodeId Id of the node to remove.
   */
  public RemoveNodeParams(int nodeId) {
    put("nodeId", nodeId);
  }
  public static final String METHOD_NAME = org.jetbrains.wip.protocol.BasicConstants.Domain.DOM + ".removeNode";

  @Override
  public String getCommand() {
    return METHOD_NAME;
  }
}