// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.outputdom;

/**
Removes node with given id.
 */
public class RemoveNodeParams extends org.jetbrains.wip.protocol.WipParams {
  /**
   @param nodeId Id of the node to remove.
   */
  public RemoveNodeParams(long nodeId) {
    //this.put("nodeId", nodeId);
  }

  public static final String METHOD_NAME = org.jetbrains.wip.protocol.BasicConstants.Domain.DOM + ".removeNode";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
