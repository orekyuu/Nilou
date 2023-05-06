package net.orekyuu.nilou.analyzer;

import com.squareup.javapoet.ClassName;
import net.orekyuu.nilou.PathSegment;
import net.orekyuu.nilou.analyzed.*;
import net.orekyuu.nilou.endpoint.QueryParam;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class JaxrsAnalyzer extends AnalyzedClassBaseHandlerAnalyzer {


    enum HttpMethod {
        GET,
        POST,
        PUT,
        PATCH,
        DELETE,
        HEAD,
        OPTIONS;

        public Optional<AnalyzedAnnotation> getAnnotation(AnalyzedAnnotations annotations) {
            return getJaxrsAnnotation(name()).apply(annotations);
        }

        public static boolean hasMethodAnnotation(AnalyzedAnnotations annotations) {
            return Arrays.stream(values()).anyMatch(it -> it.getAnnotation(annotations).isPresent());
        }
    }
    private static final Pattern pathVariablePattern = Pattern.compile("\\{(\\w+)(:\\w)?}");
    private static final Function<AnalyzedAnnotations, Optional<AnalyzedAnnotation>> GET_PATH = getJaxrsAnnotation("Path");
    private static final Function<AnalyzedAnnotations, Optional<AnalyzedAnnotation>> GET_PATH_PARAM = getJaxrsAnnotation("PathVariable");
    private static final Function<AnalyzedAnnotations, Optional<AnalyzedAnnotation>> GET_QUERY_PARAM = getJaxrsAnnotation("QueryParam");
    private static final Function<AnalyzedAnnotations, Optional<AnalyzedAnnotation>> GET_NOT_NULL_VALIDATION = getValidationAnnotation("NotNull");

    private static Function<AnalyzedAnnotations, Optional<AnalyzedAnnotation>> getJaxrsAnnotation(String name) {
        return annotations -> Stream.of("javax.ws.rs", "jakarta.ws.rs")
                .map(it -> ClassName.get(it, name))
                .map(it -> annotations.getAnnotation(it.canonicalName()).orElse(null))
                .filter(Objects::nonNull)
                .findFirst();
    }

    private static Function<AnalyzedAnnotations, Optional<AnalyzedAnnotation>> getValidationAnnotation(String name) {
        return annotations -> Stream.of("javax.validation.constraints", "jakarta.validation.constraints")
                .map(it -> ClassName.get(it, name))
                .map(it -> annotations.getAnnotation(it.canonicalName()).orElse(null))
                .filter(Objects::nonNull)
                .findFirst();
    }

    @Override
    protected boolean isHandlerMethod(AnalyzedMethod method) {
        return GET_PATH.apply(method.annotations()).isPresent() || HttpMethod.hasMethodAnnotation(method.annotations());
    }

    @Override
    protected List<PathSegment> getPathSegment(AnalyzedClass analyzedClass) {
        return getPathSegment(analyzedClass.annotations());
    }

    @Override
    protected List<PathSegment> getPathSegment(AnalyzedMethod analyzedMethod) {
        return getPathSegment(analyzedMethod.annotations());
    }

    private List<PathSegment> getPathSegment(AnalyzedAnnotations annotations) {
        String[] parts = getPathPartString(annotations);
        return Arrays.stream(parts).map(it -> {
            Matcher matcher = pathVariablePattern.matcher(it);
            if (matcher.matches()) {
                String name = matcher.group(1);
                return PathSegment.ofVariable(it, name);
            } else {
                return PathSegment.ofLiteral(it);
            }
        }).toList();
    }

    @Override
    protected List<String> getHttpMethod(AnalyzedMethod method) {
        return Stream.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "CONNECT", "TRACE", "PATCH", "HEAD")
                .filter(it -> getJaxrsAnnotation(it).apply(method.annotations()).isPresent())
                .toList();
    }

    @Override
    protected List<QueryParam> getQueryParams(AnalyzedMethod method) {
        ArrayList<QueryParam> params = new ArrayList<>();
        for (AnalyzedArgument arg : method.arguments()) {
            AnalyzedAnnotation annotation = GET_QUERY_PARAM.apply(arg.annotations()).orElse(null);
            if (annotation == null) {
                continue;
            }
            String value = ((String) annotation.getParam("value").orElse(arg.name()));
            boolean required = GET_NOT_NULL_VALIDATION.apply(arg.annotations()).isPresent();
            params.add(new QueryParam(value, arg.fqn(), required));
        }
        return params;
    }
    private String[] getPathPartString(AnalyzedAnnotations annotations) {
        return GET_PATH.apply(annotations)
                .flatMap(it -> it.getParam("value"))
                .map(it -> it instanceof String str ? str.split("/") : new String[0])
                .orElse(new String[0]);
    }
}
