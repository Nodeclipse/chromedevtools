// Generated source
package org.chromium.wip.protocol.output.debugger;

/**
 * Changes value of variable in a callframe or a closure. Either callframe or function must be specified. Object-based scopes are not supported and must be mutated manually.
 */
public final class SetVariableValueParams extends org.jetbrains.wip.WipRequest {
  /**
   * @param scopeNumber 0-based number of scope as was listed in scope chain. Only 'local', 'closure' and 'catch' scope types are allowed. Other scopes could be manipulated manually.
   * @param variableName Variable name.
   * @param newValue New variable value.
   */
  public SetVariableValueParams(int scopeNumber, String variableName, org.chromium.wip.protocol.output.runtime.CallArgumentParam newValue) {
    put("scopeNumber", scopeNumber);
    put("variableName", variableName);
    put("newValue", newValue);
  }

  /**
   * @param v Id of callframe that holds variable.
   */
  public SetVariableValueParams callFrameId(String v) {
    if (v != null) {
      put("callFrameId", v);
    }
    return this;
  }

  /**
   * @param v Object id of closure (function) that holds variable.
   */
  public SetVariableValueParams functionObjectId(String v) {
    if (v != null) {
      put("functionObjectId", v);
    }
    return this;
  }
  @Override
  public String getMethodName() {
    return "Debugger.setVariableValue";
  }
}