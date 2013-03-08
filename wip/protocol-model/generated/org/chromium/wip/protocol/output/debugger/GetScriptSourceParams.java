// Generated source
package org.chromium.wip.protocol.output.debugger;

/**
 * Returns source for the script with given id.
 */
public final class GetScriptSourceParams extends org.jetbrains.wip.WipRequestWithResponse<org.chromium.wip.protocol.input.debugger.GetScriptSourceData> {
  /**
   * @param scriptId Id of the script to get source for.
   */
  public GetScriptSourceParams(String scriptId) {
    put("scriptId", scriptId);
  }
  @Override
  public String getMethodName() {
    return "Debugger.getScriptSource";
  }

  @Override
  public org.chromium.wip.protocol.input.debugger.GetScriptSourceData parseResponse(org.jetbrains.wip.protocol.WipCommandResponse.Data data, org.chromium.wip.protocol.input.GeneratedWipProtocolReader parser) throws java.io.IOException {
    return parser.parseDebuggerGetScriptSourceData(data.getDeferredReader());
  }
}