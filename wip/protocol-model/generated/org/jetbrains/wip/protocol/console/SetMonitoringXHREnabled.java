// Generated source
package org.jetbrains.wip.protocol.console;

/**
 * Toggles monitoring of XMLHttpRequest. If <code>true</code>, console will receive messages upon each XHR issued.
 */
public final class SetMonitoringXHREnabled extends org.jetbrains.wip.protocol.WipRequest {
  /**
   * @param enabled Monitoring enabled state.
   */
  public SetMonitoringXHREnabled(boolean enabled) {
    writeBoolean("enabled", enabled);
  }
  @Override
  public String getMethodName() {
    return "Console.setMonitoringXHREnabled";
  }
}