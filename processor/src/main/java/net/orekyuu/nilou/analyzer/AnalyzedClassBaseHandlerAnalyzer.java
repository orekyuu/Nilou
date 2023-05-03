package net.orekyuu.nilou.analyzer;

import net.orekyuu.nilou.PathSegment;
import net.orekyuu.nilou.analyzed.AnalyzedClass;
import net.orekyuu.nilou.analyzed.AnalyzedMethod;
import net.orekyuu.nilou.endpoint.Endpoint;
import net.orekyuu.nilou.endpoint.HandlerMethod;
import net.orekyuu.nilou.endpoint.QueryParam;
import net.orekyuu.nilou.parser.ClassParser;

import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class AnalyzedClassBaseHandlerAnalyzer implements HandlerAnalyzer {
    @Override
    public List<HandlerMethod> analyze(TypeElement typeElement) {
        List<AnalyzedClass> analyzedClasses = typeElement.accept(new ClassParser(), new ArrayList<>());
        return analyzedClasses.stream()
                .flatMap(this::toHandlerMethod)
                .toList();
    }

    private Stream<HandlerMethod> toHandlerMethod(AnalyzedClass clazz) {
        ArrayList<HandlerMethod> result = new ArrayList<>();
        List<PathSegment> topLevel = getPathSegment(clazz);
        for (AnalyzedMethod method : clazz.methods()) {
            if (!isHandlerMethod(method)) {
                continue;
            }
            ArrayList<PathSegment> segments = new ArrayList<>();
            segments.addAll(topLevel);
            segments.addAll(getPathSegment(method));

            HandlerMethod handlerMethod = new HandlerMethod(getHttpMethod(method),
                    new Endpoint(segments, getQueryParams(method)), clazz.original(), method.original());
            result.add(handlerMethod);
        }
        return result.stream();
    }

    protected abstract boolean isHandlerMethod(AnalyzedMethod method);

    protected abstract List<PathSegment> getPathSegment(AnalyzedClass analyzedClass);
    protected abstract List<PathSegment> getPathSegment(AnalyzedMethod analyzedClass);

    protected abstract List<String> getHttpMethod(AnalyzedMethod method);

    protected abstract List<QueryParam> getQueryParams(AnalyzedMethod method);
}
