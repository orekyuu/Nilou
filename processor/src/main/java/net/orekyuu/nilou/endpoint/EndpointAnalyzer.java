package net.orekyuu.nilou.endpoint;

import net.orekyuu.nilou.analyzer.HandlerAnalyzer;
import net.orekyuu.nilou.analyzer.JaxrsAnalyzer;

import javax.lang.model.element.TypeElement;
import java.util.List;

public class EndpointAnalyzer {
    private static final List<HandlerAnalyzer> analyzers = List.of(new JaxrsAnalyzer());

    public List<HandlerMethod> analyze(TypeElement typeElement) {
        return analyzers.stream()
                .flatMap(analyzer -> analyzer.analyze(typeElement).stream())
                .toList();
    }
}
