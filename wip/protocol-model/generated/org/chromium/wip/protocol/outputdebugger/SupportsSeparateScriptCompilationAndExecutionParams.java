// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.outputdebugger;

/**
Tells whether debugger supports separate script compilation and execution.
 */
public class SupportsSeparateScriptCompilationAndExecutionParams extends org.chromium.sdk.internal.wip.protocol.output.WipParamsWithResponse<org.chromium.wip.protocol.inputdebugger.SupportsSeparateScriptCompilationAndExecutionData> {
  public SupportsSeparateScriptCompilationAndExecutionParams() {
  }

  public static final String METHOD_NAME = org.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.DEBUGGER + ".supportsSeparateScriptCompilationAndExecution";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

  @Override public org.chromium.wip.protocol.inputdebugger.SupportsSeparateScriptCompilationAndExecutionData parseResponse(org.chromium.sdk.internal.wip.protocol.input.WipCommandResponse.Data data, org.chromium.sdk.internal.wip.protocol.input.WipGeneratedParserRoot parser) throws org.chromium.sdk.internal.protocolparser.JsonProtocolParseException {
    return parser.parseDebuggerSupportsSeparateScriptCompilationAndExecutionData(data.getUnderlyingObject());
  }

}