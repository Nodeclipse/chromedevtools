// Generated source
package org.jetbrains.wip.protocol.console;

/**
 * Enables console domain, sends the messages collected so far to the client by means of the <code>messageAdded</code> notification.
 */
public final class Enable extends org.jetbrains.wip.protocol.WipRequest {

  @Override
  public String getMethodName() {
    return "Console.enable";
  }
}