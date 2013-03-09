// Generated source
package org.chromium.wip.protocol.output.dom;

/**
 * Requests that the node is sent to the caller given its path. // FIXME, use XPath
 */
public final class PushNodeByPathToFrontend extends org.jetbrains.wip.protocol.WipRequestWithResponse<org.chromium.wip.protocol.input.dom.PushNodeByPathToFrontendData> {
  /**
   * @param path Path to node in the proprietary format.
   */
  public PushNodeByPathToFrontend(String path) {
    writeString("path", path);
  }
  @Override
  public String getMethodName() {
    return "DOM.pushNodeByPathToFrontend";
  }

  @Override
  public org.chromium.wip.protocol.input.dom.PushNodeByPathToFrontendData parseResponse(org.jetbrains.wip.protocol.WipCommandResponse.Data data, org.chromium.wip.protocol.input.GeneratedWipProtocolReader parser) throws java.io.IOException {
    return parser.parseDOMPushNodeByPathToFrontendData(data.getDeferredReader());
  }
}