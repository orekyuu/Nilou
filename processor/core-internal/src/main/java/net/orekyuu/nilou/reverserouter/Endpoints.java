package net.orekyuu.nilou.reverserouter;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import net.orekyuu.nilou.DefaultEndpointUriBuilder;
import net.orekyuu.nilou.EndpointUriBuilder;
import net.orekyuu.nilou.NamingUtils;
import net.orekyuu.nilou.PathSegment;
import net.orekyuu.nilou.endpoint.Endpoint;
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
                    List<HandlerMethod> methods = it.getValue().stream().sorted().toList();
                    ClassName className = renamedMap.get(ClassName.get(element));
                    List<MethodSpec> factoryMethods = methods.stream().map(this::uriBuilderFactory).toList();

                    typeSpec.addType(TypeSpec
                            .classBuilder(className.canonicalName().replace('.', '_'))
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .addMethods(factoryMethods)
                            .build());
                });
    }

    private MethodSpec uriBuilderFactory(HandlerMethod handlerMethod) {
        CodeBlock.Builder segments = CodeBlock.builder();
        segments.add("$T<$T> segments = $T.of(", List.class, PathSegment.class, List.class);

        Endpoint endpoint = handlerMethod.endpoint();
        boolean isFirst = true;
        for (PathSegment segment : endpoint.segments()) {
            if (!isFirst) {
                segments.add(", ");
            }
            isFirst = false;
            if (segment instanceof PathSegment.Literal literal) {
                segments.add("$T.ofLiteral(\"$L\")", PathSegment.class, literal.pathPart());
            } else if (segment instanceof PathSegment.Variable variable) {
                segments.add("$T.ofVariable(\"$L\", \"$L\")", PathSegment.class, variable.raw(), variable.name());
            } else {
                throw new IllegalArgumentException("segment = " + segment);
            }
        }
        segments.add(")");

        CodeBlock.Builder bodyBuilder = CodeBlock.builder();
        bodyBuilder.addStatement(segments.build());
        bodyBuilder.addStatement("$T builder = new $T(segments)", EndpointUriBuilder.class, DefaultEndpointUriBuilder.class);
        for (PathSegment segment : endpoint.segments()) {
            if (segment instanceof PathSegment.Variable v) {
                bodyBuilder.addStatement("builder.pathParam(\"$L\", $L)", v.name(), v.name());
            }
        }

        bodyBuilder.addStatement("return builder");

        List<ParameterSpec> parameters = endpoint.segments().stream()
                .filter(it -> it instanceof PathSegment.Variable).map(it -> (PathSegment.Variable) it)
                .map(v -> ParameterSpec.builder(ClassName.get(String.class), v.name()).build())
                .toList();

        return MethodSpec.methodBuilder(handlerMethod.methodElement().getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ClassName.get(EndpointUriBuilder.class))
                .addCode(bodyBuilder.build())
                .addParameters(parameters)
                .build();
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
