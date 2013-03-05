// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.output.runtime;

/**
 * Releases remote object with given id.
 */
public class ReleaseObjectParams extends org.jetbrains.wip.protocol.WipParams {
  /**
   * @param objectId Identifier of the object to release.
   */
  public ReleaseObjectParams(String objectId) {
    put("objectId", objectId);
  }
  public static final String METHOD_NAME = org.jetbrains.wip.protocol.BasicConstants.Domain.RUNTIME + ".releaseObject";

  @Override
  public String getCommand() {
    return METHOD_NAME;
  }

}