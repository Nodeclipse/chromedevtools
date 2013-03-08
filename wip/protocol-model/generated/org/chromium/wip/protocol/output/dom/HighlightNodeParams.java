// Generated source
package org.chromium.wip.protocol.output.dom;

/**
 * Highlights DOM node with given id or with the given JavaScript object wrapper. Either nodeId or objectId must be specified.
 */
public final class HighlightNodeParams extends org.jetbrains.wip.WipRequest {
  /**
   * @param highlightConfig A descriptor for the highlight appearance.
   */
  public HighlightNodeParams(org.chromium.wip.protocol.output.dom.HighlightConfigParam highlightConfig) {
    put("highlightConfig", highlightConfig);
  }

  /**
   * @param v Identifier of the node to highlight.
   */
  public HighlightNodeParams nodeId(int v) {
    put("nodeId", v);
    return this;
  }

  /**
   * @param v JavaScript object id of the node to be highlighted.
   */
  public HighlightNodeParams objectId(String v) {
    if (v != null) {
      put("objectId", v);
    }
    return this;
  }
  @Override
  public String getMethodName() {
    return "DOM.highlightNode";
  }
}