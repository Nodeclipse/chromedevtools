// Generated source
package org.jetbrains.wip.protocol.page;

/**
 * Determines if scripts can be executed in the page.
 */
@org.chromium.protocolReader.JsonType
public interface GetScriptExecutionStatusResult {
  /**
   * Script execution status: "allowed" if scripts can be executed, "disabled" if script execution has been disabled through page settings, "forbidden" if script execution for the given page is not possible for other reasons.
   */
  Result result();

  /**
   * Script execution status: "allowed" if scripts can be executed, "disabled" if script execution has been disabled through page settings, "forbidden" if script execution for the given page is not possible for other reasons.
   */
  public enum Result {
    ALLOWED, DISABLED, FORBIDDEN
  }
}