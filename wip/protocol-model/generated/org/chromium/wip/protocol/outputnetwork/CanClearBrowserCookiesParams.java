// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.outputnetwork;

/**
Tells whether clearing browser cookies is supported.
 */
public class CanClearBrowserCookiesParams extends org.jetbrains.wip.protocol.WipParamsWithResponse<org.chromium.wip.protocol.inputnetwork.CanClearBrowserCookiesData> {
  public CanClearBrowserCookiesParams() {
  }

  public static final String METHOD_NAME = org.jetbrains.wip.protocol.BasicConstants.Domain.NETWORK + ".canClearBrowserCookies";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

  @Override public org.chromium.wip.protocol.inputnetwork.CanClearBrowserCookiesData parseResponse(org.jetbrains.wip.protocol.WipCommandResponse.Data data, org.chromium.wip.protocol.input.WipGeneratedParserRoot parser) {
    return parser.parseNetworkCanClearBrowserCookiesData(data.getUnderlyingObject());
  }

}
