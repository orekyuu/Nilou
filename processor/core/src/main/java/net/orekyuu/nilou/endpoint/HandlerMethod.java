package net.orekyuu.nilou.endpoint;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.List;

public record HandlerMethod(List<String> httpMethod, Endpoint endpoint, TypeElement handlerType, ExecutableElement methodElement) {
}
