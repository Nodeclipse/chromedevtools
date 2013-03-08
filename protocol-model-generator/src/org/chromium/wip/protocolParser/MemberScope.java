package org.chromium.wip.protocolParser;

import org.jetbrains.jsonProtocol.readerGenerator.ItemDescriptor;
import org.jetbrains.jsonProtocol.readerGenerator.WipMetamodel;

import java.io.IOException;
import java.util.List;

/**
 * Member scope is used to generate additional types that are used only from method.
 * These types will be named after this method.
 */
abstract class MemberScope implements ResolveAndGenerateScope {
  private final String memberName;
  private final ClassScope classScope;

  MemberScope(ClassScope classScope, String memberName) {
    this.classScope = classScope;
    this.memberName = memberName;
  }

  @Override
  public <T extends ItemDescriptor> QualifiedTypeData resolveType(T typedObject) {
    return classScope.generator.generator.resolveType(typedObject, this);
  }

  protected String getMemberName() {
    return memberName;
  }

  public abstract BoxableType generateEnum(String description, List<String> enumConstants);
  public abstract BoxableType generateNestedObject(String description, List<WipMetamodel.ObjectProperty> propertyList) throws IOException;

  @Override
  public String getDomainName() {
    return classScope.generator.domain.domain();
  }

  @Override
  public TypeData.Direction getTypeDirection() {
    return classScope.getTypeDirection();
  }
}
