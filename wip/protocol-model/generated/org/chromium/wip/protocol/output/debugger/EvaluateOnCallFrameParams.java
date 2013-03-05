// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.output.debugger;

/**
 * Evaluates expression on a given call frame.
 */
public class EvaluateOnCallFrameParams extends org.jetbrains.wip.protocol.WipParamsWithResponse<org.chromium.wip.protocol.input.debugger.EvaluateOnCallFrameData> {
  /**
   * @param callFrameId Call frame identifier to evaluate on.
   * @param expression Expression to evaluate.
   */
  public EvaluateOnCallFrameParams(String callFrameId, String expression) {
    put("callFrameId", callFrameId);
    put("expression", expression);
  }

  /**
   * @param v String object group name to put result into (allows rapid releasing resulting object handles using <code>releaseObjectGroup</code>).
   */
  public EvaluateOnCallFrameParams objectGroup(String v) {
    put("objectGroup", v);
    return this;
  }

  /**
   * @param v Specifies whether command line API should be available to the evaluated expression, defaults to false.
   */
  public EvaluateOnCallFrameParams includeCommandLineAPI(boolean v) {
    put("includeCommandLineAPI", v);
    return this;
  }

  /**
   * @param v Specifies whether evaluation should stop on exceptions and mute console. Overrides setPauseOnException state.
   */
  public EvaluateOnCallFrameParams doNotPauseOnExceptionsAndMuteConsole(boolean v) {
    put("doNotPauseOnExceptionsAndMuteConsole", v);
    return this;
  }

  /**
   * @param v Whether the result is expected to be a JSON object that should be sent by value.
   */
  public EvaluateOnCallFrameParams returnByValue(boolean v) {
    put("returnByValue", v);
    return this;
  }

  /**
   * @param v Whether preview should be generated for the result.
   */
  public EvaluateOnCallFrameParams generatePreview(boolean v) {
    put("generatePreview", v);
    return this;
  }
  public static final String METHOD_NAME = org.jetbrains.wip.protocol.BasicConstants.Domain.DEBUGGER + ".evaluateOnCallFrame";

  @Override
  public String getCommand() {
    return METHOD_NAME;
  }

  @Override
  public org.chromium.wip.protocol.input.debugger.EvaluateOnCallFrameData parseResponse(org.jetbrains.wip.protocol.WipCommandResponse.Data data, org.chromium.wip.protocol.input.GeneratedWipProtocolReader parser) throws java.io.IOException {
    return parser.parseDebuggerEvaluateOnCallFrameData(data.getUnderlyingObject());
  }

}