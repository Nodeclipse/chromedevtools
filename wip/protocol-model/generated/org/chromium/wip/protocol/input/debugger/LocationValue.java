// Generated source
package org.chromium.wip.protocol.input.debugger;

/**
 * Location in the source code.
 */
@org.chromium.protocolParser.JsonType
public interface LocationValue {
  /**
   * Script identifier as reported in the <code>Debugger.scriptParsed</code>.
   */
  String scriptId();
  /**
   * Line number in the script.
   */
  int lineNumber();
  /**
   * Column number in the script.
   */
  @org.chromium.protocolParser.JsonOptionalField
  int columnNumber();

}