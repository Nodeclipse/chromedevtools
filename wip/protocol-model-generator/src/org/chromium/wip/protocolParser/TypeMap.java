package org.chromium.wip.protocolParser;

import java.io.IOException;
import java.util.*;

/**
 * Keeps track of all referenced types.
 * A type may be used and resolved (generated or hard-coded).
 */
class TypeMap {
  private final Map<List<String>, TypeData> map = new HashMap<List<String>, TypeData>();
  private Map<String, DomainGenerator> domainGeneratorMap = null;
  private List<StandaloneTypeBinding> typesToGenerate = new ArrayList<StandaloneTypeBinding>();

  void setDomainGeneratorMap(Map<String, DomainGenerator> domainGeneratorMap) {
    this.domainGeneratorMap = domainGeneratorMap;
  }

  BoxableType resolve(String domainName, String typeName, TypeData.Direction direction) {
    DomainGenerator domainGenerator = domainGeneratorMap.get(domainName);
    if (domainGenerator == null) {
      throw new RuntimeException("Failed to find domain generator: " + domainName);
    }
    return getTypeData(domainName, typeName).get(direction).resolve(this, domainGenerator);
  }

  void addTypeToGenerate(StandaloneTypeBinding binding) {
    typesToGenerate.add(binding);
  }

  public void generateRequestedTypes() throws IOException {
    // Size may grow during iteration.
    //noinspection ForLoopReplaceableByForEach
    for (int i = 0; i < typesToGenerate.size(); i++) {
      typesToGenerate.get(i).generate();
    }

    for (TypeData typeData : map.values()) {
      typeData.checkComplete();
    }
  }

  TypeData getTypeData(String domainName, String typeName) {
    List<String> key = createKey(domainName, typeName);
    TypeData result = map.get(key);
    if (result == null) {
      result = new TypeData(typeName);
      map.put(key, result);
    }
    return result;
  }

  private static List<String> createKey(String domainName, String typeName) {
    return Arrays.asList(domainName, typeName);
  }
}
