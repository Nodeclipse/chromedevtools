// Generated source
package org.chromium.wip.protocol.runtime;

@org.chromium.protocolReader.JsonType
public interface PropertyPreviewValue {
  /**
   * Property name.
   */
  String name();
  /**
   * Object type.
   */
  Type type();
  /**
   * User-friendly property value string.
   */
  @org.chromium.protocolReader.JsonOptionalField
  String value();
  /**
   * Nested value preview.
   */
  @org.chromium.protocolReader.JsonOptionalField
  org.chromium.wip.protocol.runtime.ObjectPreviewValue valuePreview();
  /**
   * Object subtype hint. Specified for <code>object</code> type values only.
   */
  @org.chromium.protocolReader.JsonOptionalField
  Subtype subtype();

  /**
   * Object type.
   */
  enum Type {
    OBJECT, FUNCTION, UNDEFINED, STRING, NUMBER, BOOLEAN
  }
  /**
   * Object subtype hint. Specified for <code>object</code> type values only.
   */
  enum Subtype {
    ARRAY, NULL, NODE, REGEXP, DATE
  }
}