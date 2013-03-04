// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.outputpage;

/**
Requests that backend shows debug borders on layers
 */
public class SetShowDebugBordersParams extends org.jetbrains.wip.protocol.WipParams {
  /**
   @param show True for showing debug borders
   */
  public SetShowDebugBordersParams(boolean show) {
    //this.put("show", show);
  }

  public static final String METHOD_NAME = org.jetbrains.wip.protocol.BasicConstants.Domain.PAGE + ".setShowDebugBorders";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
