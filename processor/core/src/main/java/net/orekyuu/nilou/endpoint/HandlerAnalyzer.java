package net.orekyuu.nilou.endpoint;

import javax.lang.model.element.TypeElement;
import java.util.List;

public interface HandlerAnalyzer {
    List<HandlerMethod> analyze(TypeElement typeElement);
}
