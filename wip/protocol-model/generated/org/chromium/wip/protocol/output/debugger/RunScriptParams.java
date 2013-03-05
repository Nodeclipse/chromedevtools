// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.output.debugger;

/**
 * Runs script with given id in a given context.
 */
public class RunScriptParams extends org.jetbrains.wip.protocol.WipParamsWithResponse<org.chromium.wip.protocol.input.debugger.RunScriptData> {
  /**
   * @param scriptId Id of the script to run.
   */
  public RunScriptParams(String scriptId) {
    put("scriptId", scriptId);
  }

  /**
   * @param v Specifies in which isolated context to perform script run. Each content script lives in an isolated context and this parameter may be used to specify one of those contexts. If the parameter is omitted or 0 the evaluation will be performed in the context of the inspected page.
   */
  public RunScriptParams contextId(long v) {
    put("contextId", v);
    return this;
  }

  /**
   * @param v Symbolic group name that can be used to release multiple objects.
   */
  public RunScriptParams objectGroup(String v) {
    put("objectGroup", v);
    return this;
  }

  /**
   * @param v Specifies whether script run should stop on exceptions and mute console. Overrides setPauseOnException state.
   */
  public RunScriptParams doNotPauseOnExceptionsAndMuteConsole(boolean v) {
    put("doNotPauseOnExceptionsAndMuteConsole", v);
    return this;
  }
  public static final String METHOD_NAME = org.jetbrains.wip.protocol.BasicConstants.Domain.DEBUGGER + ".runScript";

  @Override
  public String getCommand() {
    return METHOD_NAME;
  }

  @Override
  public org.chromium.wip.protocol.input.debugger.RunScriptData parseResponse(org.jetbrains.wip.protocol.WipCommandResponse.Data data, org.chromium.wip.protocol.input.GeneratedWipProtocolReader parser) throws java.io.IOException {
    return parser.parseDebuggerRunScriptData(data.getUnderlyingObject());
  }

}