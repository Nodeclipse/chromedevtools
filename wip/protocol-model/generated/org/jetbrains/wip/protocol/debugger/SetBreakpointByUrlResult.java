// Generated source
package org.jetbrains.wip.protocol.debugger;

/**
 * Sets JavaScript breakpoint at given location specified either by URL or URL regex. Once this command is issued, all existing parsed scripts will have breakpoints resolved and returned in <code>locations</code> property. Further matching script parsing will result in subsequent <code>breakpointResolved</code> events issued. This logical breakpoint will survive page reloads.
 */
@org.chromium.protocolReader.JsonType
public interface SetBreakpointByUrlResult {
  /**
   * Id of the created breakpoint for further reference.
   */
  String breakpointId();
  /**
   * List of the locations this breakpoint resolved into upon addition.
   */
  java.util.List<org.jetbrains.wip.protocol.debugger.LocationValue> locations();

}