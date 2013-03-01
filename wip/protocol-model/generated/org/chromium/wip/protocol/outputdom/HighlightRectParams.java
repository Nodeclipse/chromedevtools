// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.outputdom;

/**
Highlights given rectangle. Coordinates are absolute with respect to the main frame viewport.
 */
public class HighlightRectParams extends org.chromium.sdk.internal.wip.protocol.output.WipParams {
  /**
   @param x X coordinate
   @param y Y coordinate
   @param width Rectangle width
   @param height Rectangle height
   @param colorOpt The highlight fill color (default: transparent).
   @param outlineColorOpt The highlight outline color (default: transparent).
   */
  public HighlightRectParams(long x, long y, long width, long height, org.chromium.wip.protocol.outputdom.RGBAParam colorOpt, org.chromium.wip.protocol.outputdom.RGBAParam outlineColorOpt) {
    this.put("x", x);
    this.put("y", y);
    this.put("width", width);
    this.put("height", height);
    if (colorOpt != null) {
      this.put("color", colorOpt);
    }
    if (outlineColorOpt != null) {
      this.put("outlineColor", outlineColorOpt);
    }
  }

  public static final String METHOD_NAME = org.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.DOM + ".highlightRect";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}