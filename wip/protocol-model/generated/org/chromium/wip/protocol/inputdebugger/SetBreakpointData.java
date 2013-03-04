// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.inputdebugger;

/**
 Sets JavaScript breakpoint at a given location.
 */
@org.chromium.protocolParser.JsonType
public interface SetBreakpointData {
  /**
   Id of the created breakpoint for further reference.
   */
  String breakpointId();

  /**
   Location this breakpoint resolved into.
   */
  org.chromium.wip.protocol.inputdebugger.LocationValue actualLocation();

}
