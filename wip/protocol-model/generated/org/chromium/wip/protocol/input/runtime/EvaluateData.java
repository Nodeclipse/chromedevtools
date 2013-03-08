// Generated source
package org.chromium.wip.protocol.input.runtime;

/**
 * Evaluates expression on global object.
 */
@org.chromium.protocolReader.JsonType
public interface EvaluateData {
  /**
   * Evaluation result.
   */
  org.chromium.wip.protocol.input.runtime.RemoteObjectValue result();
  /**
   * True if the result was thrown during the evaluation.
   */
  @org.chromium.protocolReader.JsonOptionalField
  boolean wasThrown();

}