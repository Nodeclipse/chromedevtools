package org.chromium.wip.protocolParser;

import org.jetbrains.jsonProtocol.readerGenerator.ItemDescriptor;
import org.jetbrains.jsonProtocol.readerGenerator.WipMetamodel;

import java.io.IOException;
import java.util.List;

class MyCreateStandaloneTypeBindingVisitorBase extends CreateStandaloneTypeBindingVisitorBase {
  private final String name;
  private DomainGenerator generator;

  public MyCreateStandaloneTypeBindingVisitorBase(DomainGenerator generator, WipMetamodel.StandaloneType type, String name) {
    super(generator, type);
    this.generator = generator;
    this.name = name;
  }

  @Override
  public StandaloneTypeBinding visitObject(List<WipMetamodel.ObjectProperty> properties) {
    return new StandaloneTypeBinding() {
      @Override
      public BoxableType getJavaType() {
        return new StandaloneType(Naming.ADDITIONAL_PARAM.getFullName(generator.domain.domain(), name));
      }

      @Override
      public void generate() throws IOException {
        generator.generateCommandAdditionalParam(getType());
      }

      @Override
      public TypeData.Direction getDirection() {
        return TypeData.Direction.OUTPUT;
      }
    };
  }

  @Override
  public StandaloneTypeBinding visitEnum(List<String> enumConstants) {
    throw new RuntimeException();
  }

  @Override
  public StandaloneTypeBinding visitArray(final WipMetamodel.ArrayItemType items) {
    StandaloneTypeBinding.Target target = new StandaloneTypeBinding.Target() {
      @Override
      public BoxableType resolve(final ResolveContext context) {
        ResolveAndGenerateScope resolveAndGenerateScope =
          new ResolveAndGenerateScope() {
            // This class is responsible for generating ad hoc type.
            // If we ever are to do it, we should generate into string buffer and put strings
            // inside TypeDef class.
            @Override
            public String getDomainName() {
              return generator.domain.domain();
            }

            @Override
            public TypeData.Direction getTypeDirection() {
              return TypeData.Direction.OUTPUT;
            }

            @Override
            public <T extends ItemDescriptor> QualifiedTypeData resolveType(T typedObject) {
              throw new UnsupportedOperationException();
            }

            @Override
            public BoxableType generateNestedObject(String description, List<WipMetamodel.ObjectProperty> properties)
              throws IOException {
              return context.generateNestedObject("Item", description, properties);
            }
          };
        return BoxableType.createList(generator.generator.resolveType(items, resolveAndGenerateScope).getJavaType());
      }
    };

    return generator.createTypedefTypeBinding(getType(), target, Naming.OUTPUT_TYPEDEF, TypeData.Direction.OUTPUT);
  }
}
