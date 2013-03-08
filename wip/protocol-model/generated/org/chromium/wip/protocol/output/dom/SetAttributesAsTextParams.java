// Generated source
package org.chromium.wip.protocol.output.dom;

/**
 * Sets attributes on element with given id. This method is useful when user edits some existing attribute value and types in several attribute name/value pairs.
 */
public final class SetAttributesAsTextParams extends org.jetbrains.wip.WipRequest {
  /**
   * @param nodeId Id of the element to set attributes for.
   * @param text Text with a number of attributes. Will parse this text using HTML parser.
   */
  public SetAttributesAsTextParams(int nodeId, String text) {
    put("nodeId", nodeId);
    put("text", text);
  }

  /**
   * @param v Attribute name to replace with new attributes derived from text in case text parsed successfully.
   */
  public SetAttributesAsTextParams name(String v) {
    if (v != null) {
      put("name", v);
    }
    return this;
  }
  @Override
  public String getMethodName() {
    return "DOM.setAttributesAsText";
  }
}