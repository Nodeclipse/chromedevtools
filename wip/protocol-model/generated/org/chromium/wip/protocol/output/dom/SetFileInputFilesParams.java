// Generated source
package org.chromium.wip.protocol.output.dom;

/**
 * Sets files for the given file input element.
 */
public class SetFileInputFilesParams extends org.jetbrains.wip.protocol.WipRequest {
  /**
   * @param nodeId Id of the file input node to set files for.
   * @param files Array of file paths to set.
   */
  public SetFileInputFilesParams(int nodeId, java.util.List<String> files) {
    put("nodeId", nodeId);
    writeStringList("files", files);
  }
  @Override
  public String getMethodName() {
    return "DOM.setFileInputFiles";
  }
}