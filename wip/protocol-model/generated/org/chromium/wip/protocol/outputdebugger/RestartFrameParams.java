// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.outputdebugger;

/**
Restarts particular call frame from the beginning.
 */
public class RestartFrameParams extends org.jetbrains.wip.protocol.WipParamsWithResponse<org.chromium.wip.protocol.inputdebugger.RestartFrameData> {
  /**
   @param callFrameId Call frame identifier to evaluate on.
   */
  public RestartFrameParams(String callFrameId) {
    //this.put("callFrameId", callFrameId);
  }

  public static final String METHOD_NAME = org.jetbrains.wip.protocol.BasicConstants.Domain.DEBUGGER + ".restartFrame";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

  @Override public org.chromium.wip.protocol.inputdebugger.RestartFrameData parseResponse(org.jetbrains.wip.protocol.WipCommandResponse.Data data, org.chromium.wip.protocol.input.GeneratedWipProtocolReader parser) throws java.io.IOException {
    return parser.parseDebuggerRestartFrameData(data.getUnderlyingObject());
  }

}
