package net.orekyuu.nilou.analyzed;

import com.squareup.javapoet.TypeName;

public record AnalyzedArgument(String name, TypeName fqn, AnalyzedAnnotations annotations) {
}
