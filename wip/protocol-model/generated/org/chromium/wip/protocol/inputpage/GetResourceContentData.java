// Generated source.
// Generator: org.chromium.wip.protocolParser.Generator
package org.chromium.wip.protocol.inputpage;

/**
 Returns content of the given resource.
 */
@org.chromium.sdk.internal.protocolparser.JsonType
public interface GetResourceContentData {
  /**
   Resource content.
   */
  String content();

  /**
   True, if content was served as base64.
   */
  boolean base64Encoded();

}