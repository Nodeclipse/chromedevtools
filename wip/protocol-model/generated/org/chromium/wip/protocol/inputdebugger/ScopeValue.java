// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.inputdebugger;

/**
 Scope description.
 */
@org.chromium.protocolParser.JsonType
public interface ScopeValue {
  /**
   Scope type.
   */
  Type type();

  /**
   Object representing the scope. For <code>global</code> and <code>with</code> scopes it represents the actual object; for the rest of the scopes, it is artificial transient object enumerating scope variables as its properties.
   */
  org.chromium.wip.protocol.inputruntime.RemoteObjectValue object();

  /**
   Scope type.
   */
  public enum Type {
    GLOBAL,
    LOCAL,
    WITH,
    CLOSURE,
    CATCH,
  }
}
