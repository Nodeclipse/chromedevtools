// Generated source
package org.jetbrains.wip.protocol.debugger;

/**
 * Runs script with given id in a given context.
 */
public final class RunScript extends org.jetbrains.wip.protocol.WipRequest implements org.jetbrains.jsonProtocol.RequestWithResponse<org.jetbrains.wip.protocol.debugger.RunScriptResult, org.jetbrains.wip.protocol.ProtocolReponseReader> {
  /**
   * @param scriptId Id of the script to run.
   */
  public RunScript(String scriptId) {
    writeString("scriptId", scriptId);
  }

  /**
   * @param v Specifies in which isolated context to perform script run. Each content script lives in an isolated context and this parameter may be used to specify one of those contexts. If the parameter is omitted or 0 the evaluation will be performed in the context of the inspected page.
   */
  public RunScript contextId(int v) {
    writeInt("contextId", v);
    return this;
  }

  /**
   * @param v Symbolic group name that can be used to release multiple objects.
   */
  public RunScript objectGroup(String v) {
    if (v != null) {
      writeString("objectGroup", v);
    }
    return this;
  }

  /**
   * @param v Specifies whether script run should stop on exceptions and mute console. Overrides setPauseOnException state.
   */
  public RunScript doNotPauseOnExceptionsAndMuteConsole(boolean v) {
    writeBoolean("doNotPauseOnExceptionsAndMuteConsole", v);
    return this;
  }
  @Override
  public String getMethodName() {
    return "Debugger.runScript";
  }

  @Override
  public RunScriptResult readResult(com.google.gson.stream.JsonReaderEx jsonReader, org.jetbrains.wip.protocol.ProtocolReponseReader reader) {
    return reader.readDebuggerRunScriptResult(jsonReader);
  }
}