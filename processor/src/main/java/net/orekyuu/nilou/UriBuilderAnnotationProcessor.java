package net.orekyuu.nilou;

import net.orekyuu.nilou.endpoint.EndpointAnalyzer;
import net.orekyuu.nilou.endpoint.HandlerMethod;
import net.orekyuu.nilou.reverserouter.Endpoints;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class UriBuilderAnnotationProcessor extends AbstractProcessor {
  private final EndpointAnalyzer analyzer = new EndpointAnalyzer();
  private final Endpoints endpoints = new Endpoints();
  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for (Element rootElement : roundEnv.getElementsAnnotatedWith(UriBuilder.class)) {
      if (rootElement instanceof TypeElement type) {
        List<HandlerMethod> analyzed = analyzer.analyze(type);
        endpoints.addEndpoint(type, analyzed);
      }
    }

    if (roundEnv.processingOver()) {
      try {
        endpoints.endpointsFile().writeTo(processingEnv.getFiler());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    return true;
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Set.of(UriBuilder.class.getCanonicalName());
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.RELEASE_17;
  }
}
