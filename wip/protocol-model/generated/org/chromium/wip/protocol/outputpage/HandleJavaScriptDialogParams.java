// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.outputpage;

/**
Accepts or dismisses a JavaScript initiated dialog (alert, confirm, prompt, or onbeforeunload).
 */
public class HandleJavaScriptDialogParams extends org.chromium.sdk.internal.wip.protocol.output.WipParams {
  /**
   @param accept Whether to accept or dismiss the dialog.
   */
  public HandleJavaScriptDialogParams(boolean accept) {
    this.put("accept", accept);
  }

  public static final String METHOD_NAME = org.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.PAGE + ".handleJavaScriptDialog";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}