// Generated source
package org.jetbrains.wip.protocol.network;

/**
 * Specifies whether to always send extra HTTP headers with the requests from this page.
 */
public final class SetExtraHTTPHeaders extends org.jetbrains.wip.protocol.WipRequest {
  /**
   * @param headers Map with extra HTTP headers.
   */
  public SetExtraHTTPHeaders(org.jetbrains.wip.protocol.network.Headers headers) {
    writeMessage("headers", headers);
  }
  @Override
  public String getMethodName() {
    return "Network.setExtraHTTPHeaders";
  }
}