// Generated source
package org.jetbrains.wip.protocol.dom;

/**
 * Searches for a given string in the DOM tree. Use <code>getSearchResults</code> to access search results or <code>cancelSearch</code> to end this search session.
 */
public final class PerformSearch extends org.jetbrains.wip.protocol.WipRequest implements org.jetbrains.jsonProtocol.RequestWithResponse<org.jetbrains.wip.protocol.dom.PerformSearchResult, org.jetbrains.wip.protocol.ProtocolReponseReader> {
  /**
   * @param query Plain text or query selector or XPath search query.
   */
  public PerformSearch(String query) {
    writeString("query", query);
  }
  @Override
  public String getMethodName() {
    return "DOM.performSearch";
  }

  @Override
  public PerformSearchResult readResult(com.google.gson.stream.JsonReaderEx jsonReader, org.jetbrains.wip.protocol.ProtocolReponseReader reader) {
    return reader.readDOMPerformSearchResult(jsonReader);
  }
}