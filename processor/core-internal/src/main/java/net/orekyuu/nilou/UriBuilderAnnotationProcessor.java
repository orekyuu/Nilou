package net.orekyuu.nilou;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Set;

public class UriBuilderAnnotationProcessor extends AbstractProcessor {

  private ClassName endpointsClassName = ClassName.get("net.orekyuu.nilou", "Endpoints");
  private TypeSpec.Builder builder = TypeSpec.classBuilder(endpointsClassName)
          .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for (Element rootElement : roundEnv.getElementsAnnotatedWith(UriBuilder.class)) {
      if (rootElement instanceof TypeElement type) {
        if (builder.originatingElements.contains(rootElement)) {
          continue;
        }

        builder.addField(FieldSpec.builder(ClassName.get(type), type.getSimpleName().toString(), Modifier.PUBLIC, Modifier.STATIC).build());
        builder.addOriginatingElement(rootElement);
      }
    }

    if (roundEnv.getRootElements().isEmpty()) {
      try {
        JavaFile javaFile = JavaFile.builder(endpointsClassName.packageName(), builder.build()).build();
        javaFile.writeTo(processingEnv.getFiler());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    return true;
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Set.of("*");
  }
}
