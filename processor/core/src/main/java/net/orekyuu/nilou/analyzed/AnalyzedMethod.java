package net.orekyuu.nilou.analyzed;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

public record AnalyzedMethod(ExecutableElement original, String name, TypeName returnType, AnalyzedAnnotations annotations, List<AnalyzedArgument> arguments) {
}
