// Generated source.
// Generator: org.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/!svn/bc/93101/trunk/Source/WebCore/inspector/Inspector.json@93101

package org.chromium.sdk.internal.wip.protocol.input.inspector;

@org.chromium.sdk.internal.protocolparser.JsonType
public interface FrontendReusedEventData {
  public static final org.chromium.sdk.internal.wip.protocol.input.WipEventType<org.chromium.sdk.internal.wip.protocol.input.inspector.FrontendReusedEventData> TYPE
      = new org.chromium.sdk.internal.wip.protocol.input.WipEventType<org.chromium.sdk.internal.wip.protocol.input.inspector.FrontendReusedEventData>("Inspector.frontendReused", org.chromium.sdk.internal.wip.protocol.input.inspector.FrontendReusedEventData.class) {
    @Override public org.chromium.sdk.internal.wip.protocol.input.inspector.FrontendReusedEventData parse(org.chromium.sdk.internal.wip.protocol.input.WipGeneratedParserRoot parser, org.json.simple.JSONObject obj) throws org.chromium.sdk.internal.protocolparser.JsonProtocolParseException {
      return parser.parseInspectorFrontendReusedEventData(obj);
    }
  };
}
