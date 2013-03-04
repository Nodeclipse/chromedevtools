// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.outputdebugger;

/**
Runs script with given id in a given context.
 */
public class RunScriptParams extends org.jetbrains.wip.protocol.WipParamsWithResponse<org.chromium.wip.protocol.inputdebugger.RunScriptData> {
  /**
   @param scriptId Id of the script to run.
   @param contextIdOpt Specifies in which isolated context to perform script run. Each content script lives in an isolated context and this parameter may be used to specify one of those contexts. If the parameter is omitted or 0 the evaluation will be performed in the context of the inspected page.
   @param objectGroupOpt Symbolic group name that can be used to release multiple objects.
   @param doNotPauseOnExceptionsAndMuteConsoleOpt Specifies whether script run should stop on exceptions and mute console. Overrides setPauseOnException state.
   */
  public RunScriptParams(String/*See org.chromium.wip.protocol.commondebugger.ScriptIdTypedef*/ scriptId, Long/*See org.chromium.wip.protocol.commonruntime.ExecutionContextIdTypedef*/ contextIdOpt, String objectGroupOpt, Boolean doNotPauseOnExceptionsAndMuteConsoleOpt) {
    this.put("scriptId", scriptId);
    if (contextIdOpt != null) {
      this.put("contextId", contextIdOpt);
    }
    if (objectGroupOpt != null) {
      this.put("objectGroup", objectGroupOpt);
    }
    if (doNotPauseOnExceptionsAndMuteConsoleOpt != null) {
      this.put("doNotPauseOnExceptionsAndMuteConsole", doNotPauseOnExceptionsAndMuteConsoleOpt);
    }
  }

  public static final String METHOD_NAME = org.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.DEBUGGER + ".runScript";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

  @Override public org.chromium.wip.protocol.inputdebugger.RunScriptData parseResponse(org.jetbrains.wip.protocol.WipCommandResponse.Data data, org.jetbrains.wip.protocol.WipGeneratedParserRoot parser) {
    return parser.parseDebuggerRunScriptData(data.getUnderlyingObject());
  }

}
