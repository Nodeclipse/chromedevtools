// Generated source
package org.jetbrains.wip.protocol.debugger;

/**
 * Returns detailed informtation on given function.
 */
@org.chromium.protocolReader.JsonType
public interface GetFunctionDetailsResult {
  /**
   * Information about the function.
   */
  org.jetbrains.wip.protocol.debugger.FunctionDetailsValue details();

}