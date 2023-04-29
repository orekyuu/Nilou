package net.orekyuu.nilou;

import com.squareup.javapoet.ClassName;
import net.orekyuu.nilou.analyzed.*;
import net.orekyuu.nilou.endpoint.Endpoint;
import net.orekyuu.nilou.endpoint.HandlerAnalyzer;
import net.orekyuu.nilou.endpoint.HandlerMethod;
import net.orekyuu.nilou.endpoint.QueryParam;
import net.orekyuu.nilou.parser.ClassParser;

import javax.lang.model.element.TypeElement;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class JaxrsAnalyzer implements HandlerAnalyzer {


    private static final Pattern pathVariablePattern = Pattern.compile("\\{(\\w+)(:\\w)?}");
    private static final Function<AnalyzedAnnotations, Optional<AnalyzedAnnotation>> GET_PATH = getAnnotation("Path");
    private static final Function<AnalyzedAnnotations, Optional<AnalyzedAnnotation>> GET_PATH_PARAM = getAnnotation("PathVariable");
    private static final Function<AnalyzedAnnotations, Optional<AnalyzedAnnotation>> GET_QUERY_PARAM = getAnnotation("QueryParam");

    @Override
    public List<HandlerMethod> analyze(TypeElement typeElement) {
        List<AnalyzedClass> analyzedClasses = typeElement.accept(new ClassParser(), new ArrayList<>());
        return analyzedClasses.stream()
                .flatMap(this::toHandlerMethod)
                .toList();
    }

    private Stream<HandlerMethod> toHandlerMethod(AnalyzedClass clazz) {
        ArrayList<HandlerMethod> result = new ArrayList<>();
        String[] topLevel = getPathPartString(clazz.annotations());
        for (AnalyzedMethod method : clazz.methods()) {
            Optional<AnalyzedAnnotation> methodLevel = GET_PATH.apply(method.annotations());
            if (methodLevel.isEmpty()) {
                continue;
            }
            List<PathSegment> segments = getPathPart(topLevel, method);
            HandlerMethod handlerMethod = new HandlerMethod(List.of("TODO"), new Endpoint(segments, getQueryParams(method)), clazz.original(), method.original());
            result.add(handlerMethod);
        }
        return result.stream();
    }

    private static Function<AnalyzedAnnotations, Optional<AnalyzedAnnotation>> getAnnotation(String name) {
        return annotations -> Stream.of("javax.ws.rs", "jakarta.ws.rs")
                .map(it -> ClassName.get(it, name))
                .map(it -> annotations.getAnnotation(it.canonicalName()).orElse(null))
                .filter(Objects::nonNull)
                .findFirst();
    }

    private String[] getPathPartString(AnalyzedAnnotations annotations) {
        return GET_PATH.apply(annotations)
                .flatMap(it -> it.getParam("value"))
                .map(it -> it instanceof String str ? str.split("/") : new String[0])
                .orElse(new String[0]);
    }



    private List<PathSegment> getPathPart(String[] classLevelPathPart, AnalyzedMethod method) {

        var parts = Stream.concat(Arrays.stream(classLevelPathPart), Arrays.stream(getPathPartString(method.annotations())));
        return parts.map(it -> {
            Matcher matcher = pathVariablePattern.matcher(it);
            if (matcher.matches()) {
                String name = matcher.group(1);
                return PathSegment.ofVariable(it, name);
            } else {
                return PathSegment.ofLiteral(it);
            }
        }).toList();
    }

    private List<QueryParam> getQueryParams(AnalyzedMethod method) {
        ArrayList<QueryParam> params = new ArrayList<>();
        for (AnalyzedArgument arg : method.arguments()) {
            AnalyzedAnnotation annotation = GET_PATH_PARAM.apply(method.annotations()).orElse(null);
            if (annotation == null) {
                continue;
            }
            String value = ((String) annotation.getParam("value").orElse(arg.name()));
            boolean required = false;
            params.add(new QueryParam(value, arg.fqn(), required));
        }
        return params;
    }
}
