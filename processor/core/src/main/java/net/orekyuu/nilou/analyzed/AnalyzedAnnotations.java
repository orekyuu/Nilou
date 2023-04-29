package net.orekyuu.nilou.analyzed;

import java.util.List;
import java.util.Optional;

public record AnalyzedAnnotations(List<AnalyzedAnnotation> annotations) {
    public boolean hasAnnotation(String annotationFqn) {
        return getAnnotation(annotationFqn).isPresent();
    }

    public Optional<AnalyzedAnnotation> getAnnotation(String annotationFqn) {
        return annotations().stream()
                .filter(it -> it.fqn().toString().equals(annotationFqn))
                .findAny();
    }
}
