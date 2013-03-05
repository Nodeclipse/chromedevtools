package org.chromium.wip.schemaParser;

import org.chromium.protocolParser.JsonField;
import org.chromium.protocolParser.JsonOptionalField;
import org.chromium.protocolParser.JsonType;

import java.util.List;

/**
 * Defines schema of WIP metamodel defined in
 * "http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json".
 */
public interface WipMetamodel {
  @JsonType
  interface Root {
    @JsonOptionalField
    Version version();
    List<Domain> domains();
  }

  @JsonType
  interface Version {
    String major();
    String minor();
  }

  @JsonType
  interface Domain {
    String domain();

    @JsonOptionalField
    List<StandaloneType> types();

    List<Command> commands();

    @JsonOptionalField
    List<Event> events();

    @JsonOptionalField String description();

    @JsonOptionalField boolean hidden();
  }

  @JsonType
  interface Command {
    String name();
    @JsonOptionalField List<Parameter> parameters();
    @JsonOptionalField List<Parameter> returns();

    @JsonOptionalField String description();

    @JsonOptionalField boolean hidden();
    @JsonOptionalField boolean async();
  }

  @JsonType
  interface Parameter {
    String name();

    @JsonOptionalField
    String type();

    @JsonOptionalField
    ArrayItemType items();

    @JsonField(jsonLiteralName="enum")
    @JsonOptionalField
    List<String> getEnum();

    // This is unparsable.
    @JsonOptionalField
    List<ObjectProperty> properties();

    @JsonOptionalField
    @JsonField(jsonLiteralName="$ref")
    String ref();

    @JsonOptionalField
    boolean optional();

    @JsonOptionalField String description();

    @JsonOptionalField boolean hidden();
  }

  @JsonType interface Event {
    String name();
    @JsonOptionalField List<Parameter> parameters();

    @JsonOptionalField String description();

    @JsonOptionalField boolean hidden();
  }

  @JsonType interface StandaloneType {
    String id();

    @JsonOptionalField String description();

    String type();

    @JsonOptionalField boolean hidden();

    @JsonOptionalField List<ObjectProperty> properties();

    @JsonField(jsonLiteralName="enum")
    @JsonOptionalField
    List<String> getEnum();

    @JsonOptionalField
    ArrayItemType items();
  }

  @JsonType interface ObjectProperty {
    String name();

    @JsonOptionalField
    String description();

    @JsonOptionalField
    boolean optional();

    @JsonOptionalField
    String type();

    @JsonOptionalField
    ArrayItemType items();

    @JsonField(jsonLiteralName="$ref")
    @JsonOptionalField
    String ref();

    @JsonField(jsonLiteralName="enum")
    @JsonOptionalField
    List<String> getEnum();

    @JsonOptionalField boolean hidden();
  }

  @JsonType interface ArrayItemType {
    @JsonOptionalField
    String description();

    @JsonOptionalField
    boolean optional();

    @JsonOptionalField
    String type();

    @JsonOptionalField
    ArrayItemType items();

    @JsonField(jsonLiteralName="$ref")
    @JsonOptionalField
    String ref();

    @JsonField(jsonLiteralName="enum")
    @JsonOptionalField
    List<String> getEnum();

    @JsonOptionalField
    List<ObjectProperty> properties();
  }

  String STRING_TYPE = "string";
  String INTEGER_TYPE = "integer";
  String NUMBER_TYPE = "number";
  String BOOLEAN_TYPE = "boolean";
  String OBJECT_TYPE = "object";
  String ARRAY_TYPE = "array";
  String UNKNOWN_TYPE = "unknown";
  String ANY_TYPE = "any";
}