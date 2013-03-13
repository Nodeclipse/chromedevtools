package org.chromium.wip.protocolParser;

import org.chromium.protocolparser.DynamicParserImpl;
import org.chromium.protocolparser.ReaderGenerator;
import org.chromium.wip.protocol.GeneratedReaderInterfaceList;
import org.jetbrains.wip.protocol.WipProtocolReader;

public class WipReaderGenerator extends ReaderGenerator {
  public static void main(String[] args) {
    mainImpl(args, new GenerateConfiguration("org.chromium.wip.protocol", "WipProtocolReaderImpl",
                                             new DynamicParserImpl<WipProtocolReader>(true, WipProtocolReader.class,
                                                                                      GeneratedReaderInterfaceList.LIST)));
  }
}