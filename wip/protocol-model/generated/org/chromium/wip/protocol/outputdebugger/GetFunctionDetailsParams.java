// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.outputdebugger;

/**
Returns detailed informtation on given function.
 */
public class GetFunctionDetailsParams extends org.jetbrains.wip.protocol.WipParamsWithResponse<org.chromium.wip.protocol.inputdebugger.GetFunctionDetailsData> {
  /**
   @param functionId Id of the function to get location for.
   */
  public GetFunctionDetailsParams(String/*See org.chromium.wip.protocol.commonruntime.RemoteObjectIdTypedef*/ functionId) {
    this.put("functionId", functionId);
  }

  public static final String METHOD_NAME = org.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.DEBUGGER + ".getFunctionDetails";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

  @Override public org.chromium.wip.protocol.inputdebugger.GetFunctionDetailsData parseResponse(org.jetbrains.wip.protocol.WipCommandResponse.Data data, org.jetbrains.wip.protocol.WipGeneratedParserRoot parser) {
    return parser.parseDebuggerGetFunctionDetailsData(data.getUnderlyingObject());
  }

}
