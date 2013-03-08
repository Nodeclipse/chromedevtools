package org.chromium.wip.protocolParser;

import org.chromium.protocolparser.Enums;
import org.chromium.protocolparser.TextOutput;
import org.jetbrains.jsonProtocol.readerGenerator.WipMetamodel;

import java.io.IOException;
import java.util.List;

class InputClassScope extends ClassScope {
  InputClassScope(DomainGenerator generator, NamePath namePath) {
    super(generator, namePath);
  }

  public void generateMainJsonProtocolInterfaceBody(TextOutput out, List<WipMetamodel.Parameter> parameters) throws IOException {
    if (parameters != null) {
      for (WipMetamodel.Parameter param : parameters) {
        if (param.description() != null) {
          out.doc(param.description());
        }

        String methodName = Generator.generateMethodNameSubstitute(param.name(), out);
        QualifiedTypeData paramTypeData = newMemberScope(param.name()).resolveType(param);
        paramTypeData.writeAnnotations(out);
        out.append(paramTypeData.getJavaType().getShortText(getClassContextNamespace())).space().append(methodName).append("();").newLine();
      }
    }
  }

  void generateStandaloneTypeBody(TextOutput out, List<WipMetamodel.ObjectProperty> properties) throws IOException {
    if (properties != null) {
      for (WipMetamodel.ObjectProperty objectProperty : properties) {
        String propertyName = objectProperty.name();

        if (objectProperty.description() != null) {
          out.doc(objectProperty.description());
        }

        String methodName = Generator.generateMethodNameSubstitute(propertyName, out);
        MemberScope memberScope = newMemberScope(propertyName);
        QualifiedTypeData propertyTypeData = memberScope.resolveType(objectProperty);
        propertyTypeData.writeAnnotations(out);

        out.append(propertyTypeData.getJavaType().getShortText(getClassContextNamespace()) + " " + methodName + "();").newLine();
      }
    }
  }

  @Override
  protected TypeData.Direction getTypeDirection() {
    return TypeData.Direction.INPUT;
  }

  private MemberScope newMemberScope(String memberName) {
    return new InputMemberScope(memberName);
  }

  class InputMemberScope extends MemberScope {
    InputMemberScope(String memberName) {
      super(InputClassScope.this, memberName);
    }

    @Override
    public BoxableType generateEnum(final String description, final List<String> enumConstants) {
      final String enumName = Generator.capitalizeFirstChar(getMemberName());
      addMember(new TextOutConsumer() {
        @Override
        public void append(TextOutput out) {
          out.newLine().doc(description);
          Enums.appendEnums(enumConstants, enumName, true, out);
        }
      });
      return new StandaloneType(new NamePath(enumName, getClassContextNamespace()));
    }

    @Override
    public BoxableType generateNestedObject(final String description, final List<WipMetamodel.ObjectProperty> propertyList) throws IOException {
      final String objectName = Generator.capitalizeFirstChar(getMemberName());
      addMember(new TextOutConsumer() {
        @Override
        public void append(TextOutput out) throws IOException {
          out.newLine().doc(description);
          if (propertyList == null) {
            out.append("@org.chromium.protocolReader.JsonType(allowsOtherProperties=true)").newLine();
            out.append("public interface ").append(objectName).append(" extends org.jetbrains.jsonProtocol.JsonObjectBased").openBlock();
          }
          else {
            out.append("@org.chromium.protocolReader.JsonType").newLine();
            out.append("public interface ").append(objectName).openBlock();
            for (WipMetamodel.ObjectProperty property : propertyList) {
              out.doc(property.description());

              String methodName = Generator.generateMethodNameSubstitute(property.name(), out);
              MemberScope memberScope = newMemberScope(property.name());
              QualifiedTypeData propertyTypeData = memberScope.resolveType(property);
              propertyTypeData.writeAnnotations(out);

              out.append(propertyTypeData.getJavaType().getShortText(getClassContextNamespace()) + " " + methodName + "();").newLine();
            }
          }
          out.closeBlock();
        }
      });
      return new StandaloneType(new NamePath(objectName, getClassContextNamespace()));
    }
  }
}
