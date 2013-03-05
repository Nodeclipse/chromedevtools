// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.output.page;

/**
 * Requests that backend enables continuous painting
 */
public class SetContinuousPaintingEnabledParams extends org.jetbrains.wip.protocol.WipParams {
  /**
   * @param enabled True for enabling cointinuous painting
   */
  public SetContinuousPaintingEnabledParams(boolean enabled) {
    put("enabled", enabled);
  }
  public static final String METHOD_NAME = org.jetbrains.wip.protocol.BasicConstants.Domain.PAGE + ".setContinuousPaintingEnabled";

  @Override
  public String getCommand() {
    return METHOD_NAME;
  }

}