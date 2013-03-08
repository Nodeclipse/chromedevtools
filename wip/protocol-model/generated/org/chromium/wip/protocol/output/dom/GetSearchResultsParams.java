// Generated source
package org.chromium.wip.protocol.output.dom;

/**
 * Returns search results from given <code>fromIndex</code> to given <code>toIndex</code> from the sarch with the given identifier.
 */
public final class GetSearchResultsParams extends org.jetbrains.wip.WipRequestWithResponse<org.chromium.wip.protocol.input.dom.GetSearchResultsData> {
  /**
   * @param searchId Unique search session identifier.
   * @param fromIndex Start index of the search result to be returned.
   * @param toIndex End index of the search result to be returned.
   */
  public GetSearchResultsParams(String searchId, int fromIndex, int toIndex) {
    put("searchId", searchId);
    put("fromIndex", fromIndex);
    put("toIndex", toIndex);
  }
  @Override
  public String getMethodName() {
    return "DOM.getSearchResults";
  }

  @Override
  public org.chromium.wip.protocol.input.dom.GetSearchResultsData parseResponse(org.jetbrains.wip.protocol.WipCommandResponse.Data data, org.chromium.wip.protocol.input.GeneratedWipProtocolReader parser) throws java.io.IOException {
    return parser.parseDOMGetSearchResultsData(data.getDeferredReader());
  }
}