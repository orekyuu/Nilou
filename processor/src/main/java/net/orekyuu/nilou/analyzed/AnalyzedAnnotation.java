package net.orekyuu.nilou.analyzed;


import com.squareup.javapoet.TypeName;

import java.util.List;
import java.util.Optional;

public record AnalyzedAnnotation(TypeName fqn, List<AnalyzedParameter> params) {

    public Optional<Object> getParam(String name) {
        return params.stream()
                .filter(it -> it.name().equals(name))
                .map(AnalyzedParameter::param)
                .findAny();
    }
}
