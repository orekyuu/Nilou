package net.orekyuu.nilou.parser;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import net.orekyuu.nilou.analyzed.*;

import javax.lang.model.element.*;
import javax.lang.model.util.ElementScanner14;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassParser extends ElementScanner14<List<AnalyzedClass>, List<AnalyzedClass>> {

    TypeElement current;
    Map<TypeElement, List<ExecutableElement>> map = new HashMap<>();

    @Override
    public List<AnalyzedClass> visitType(TypeElement e, List<AnalyzedClass> arg) {
        current = e;
        map.put(current, new ArrayList<>());
        super.visitType(e, arg);

        arg.add(create());
        return arg;
    }

    @Override
    public List<AnalyzedClass> visitExecutable(ExecutableElement e, List<AnalyzedClass> arg) {
        var elements = map.get(current);
        elements.add(e);
        return super.visitExecutable(e, arg);
    }


    AnalyzedClass create() {
        var annotations = current.getAnnotationMirrors().stream().map(this::toAnnotation).toList();
        var methods = map.get(current).stream()
                .filter(it -> it.getKind() == ElementKind.METHOD)
                .map(this::toMethod)
                .toList();
        return new AnalyzedClass(current, ClassName.get(current), new AnalyzedAnnotations(annotations), methods);
    }

    AnalyzedMethod toMethod(ExecutableElement e) {
        var returnType = ClassName.get(e.getReturnType());
        var methodName = e.getSimpleName().toString();
        var args = e.getParameters().stream().map(this::toArgument).toList();
        var annotations = e.getAnnotationMirrors().stream().map(this::toAnnotation).toList();
        return new AnalyzedMethod(e, methodName, returnType, new AnalyzedAnnotations(annotations), args);
    }

    AnalyzedArgument toArgument(VariableElement e) {
        var paramName = e.getSimpleName().toString();
        var paramTypeName = ClassName.get(e.asType());
        var annotations = e.getAnnotationMirrors().stream().map(this::toAnnotation).toList();
        return new AnalyzedArgument(paramName, paramTypeName, new AnalyzedAnnotations(annotations));
    }

    AnalyzedAnnotation toAnnotation(AnnotationMirror mirror) {
        TypeName annotationType = TypeName.get(mirror.getAnnotationType());
        var params = mirror.getElementValues().entrySet().stream().map(e -> toParam(e.getKey(), e.getValue())).toList();
        return new AnalyzedAnnotation(annotationType, params);
    }

    AnalyzedParameter toParam(ExecutableElement e, AnnotationValue value) {
        TypeName typeName = TypeName.get(e.getReturnType());
        String name = e.getSimpleName().toString();
        return new AnalyzedParameter(name, typeName, value.getValue());
    }
}
