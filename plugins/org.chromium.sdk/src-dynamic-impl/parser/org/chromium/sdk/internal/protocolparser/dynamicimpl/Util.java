package org.chromium.sdk.internal.protocolparser.dynamicimpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

public class Util {
  public static final String BASE_PACKAGE = "org.chromium.sdk.internal.protocolparser";
  public static final String THROWS_CLAUSE = " throws IOException";
  public static final char TYPE_FACTORY_NAME_POSTFIX = 'F';
  public static final String READER_NAME = "reader";

  /**
   * Generate Java type name of the passed type. Type may be parameterized.
   */
  public static void writeJavaTypeName(Type arg, TextOutput out) {
    if (arg instanceof Class) {
      out.append(((Class<?>)arg).getCanonicalName());
    }
    else if (arg instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType)arg;
      writeJavaTypeName(parameterizedType.getRawType(), out);
      out.append('<');
      Type[] params = parameterizedType.getActualTypeArguments();
      for (int i = 0; i < params.length; i++) {
        if (i != 0) {
          out.comma();
        }
        writeJavaTypeName(params[i], out);
      }
      out.append('>');
    }
    else if (arg instanceof WildcardType) {
      WildcardType wildcardType = (WildcardType)arg;
      Type[] upperBounds = wildcardType.getUpperBounds();
      if (upperBounds == null) {
        throw new RuntimeException();
      }
      if (upperBounds.length != 1) {
        throw new RuntimeException();
      }
      out.append("? extends ");
      writeJavaTypeName(upperBounds[0], out);
    }
    else {
      out.append(String.valueOf(arg));
    }
  }
}