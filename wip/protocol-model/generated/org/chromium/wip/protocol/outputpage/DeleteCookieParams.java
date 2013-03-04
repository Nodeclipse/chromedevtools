// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.outputpage;

/**
Deletes browser cookie with given name, domain and path.
 */
public class DeleteCookieParams extends org.jetbrains.wip.protocol.WipParams {
  /**
   @param cookieName Name of the cookie to remove.
   @param url URL to match cooke domain and path.
   */
  public DeleteCookieParams(String cookieName, String url) {
    //this.put("cookieName", cookieName);
    //this.put("url", url);
  }

  public static final String METHOD_NAME = org.jetbrains.wip.protocol.BasicConstants.Domain.PAGE + ".deleteCookie";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
