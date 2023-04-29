package net.orekyuu.nilou.analyzed;

import com.squareup.javapoet.ClassName;

import javax.lang.model.element.TypeElement;
import java.util.List;

public record AnalyzedClass(TypeElement original, ClassName name, AnalyzedAnnotations annotations, List<AnalyzedMethod> methods) {
    public List<AnalyzedMethod> findAnnotatedMethods(String annotationFqn) {
        return methods().stream().filter(it -> it.annotations().hasAnnotation(annotationFqn)).toList();
    }
}
