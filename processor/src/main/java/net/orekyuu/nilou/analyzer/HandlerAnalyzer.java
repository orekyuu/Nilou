package net.orekyuu.nilou.analyzer;

import net.orekyuu.nilou.endpoint.HandlerMethod;

import javax.lang.model.element.TypeElement;
import java.util.List;

public interface HandlerAnalyzer {
    List<HandlerMethod> analyze(TypeElement typeElement);
}
