package net.orekyuu.nilou.reverserouter;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import net.orekyuu.nilou.NamingUtils;
import net.orekyuu.nilou.endpoint.HandlerMethod;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Endpoints implements GenerateCode {

    private static final ClassName CLASS_NAME = ClassName.get("net.orekyuu.nilou", "Endpoints");
    private final Map<TypeElement, List<HandlerMethod>> endpoints = new HashMap<>();

    public void addEndpoint(TypeElement element, List<HandlerMethod> methods) {
        endpoints.put(element, methods);
    }

    public JavaFile endpointsFile() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(CLASS_NAME);
        generate(builder);
        return JavaFile.builder(CLASS_NAME.packageName(), builder.build()).build();
    }

    @Override
    public void generate(TypeSpec.Builder typeSpec) {
        Map<ClassName, ClassName> renamedMap = createRenamedMap();

        typeSpec.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        endpoints.entrySet().stream()
                .sorted(Comparator.comparing(it -> it.getKey().getQualifiedName().toString()))
                .forEachOrdered(it -> {
                    TypeElement element = it.getKey();
                    ClassName className = renamedMap.get(ClassName.get(element));
                    typeSpec.addType(TypeSpec
                            .classBuilder(className.canonicalName().replace('.', '_'))
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .build());
                });
    }

    @Override
    public void generate(List<JavaFile> files) {

    }

    private Map<ClassName, ClassName> createRenamedMap() {
        List<ClassName> originalNames = endpoints.values().stream()
                .flatMap(Collection::stream)
                .map(it -> ClassName.get(it.handlerType()))
                .distinct()
                .toList();
        List<ClassName> shorted = NamingUtils.shortClasNames(originalNames.stream().map(ClassName::canonicalName).toList()).stream()
                .map(ClassName::bestGuess)
                .distinct()
                .toList();
        return IntStream.range(0, Math.min(originalNames.size(), shorted.size()))
                .mapToObj(i -> Map.entry(originalNames.get(i), shorted.get(i)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
