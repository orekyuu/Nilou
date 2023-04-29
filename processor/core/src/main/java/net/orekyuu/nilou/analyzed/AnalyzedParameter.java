package net.orekyuu.nilou.analyzed;

import com.squareup.javapoet.TypeName;

public record AnalyzedParameter(String name, TypeName fqn, Object param) {
}
