package net.orekyuu.nilou.reverserouter;

import com.squareup.javapoet.*;
import net.orekyuu.nilou.DefaultEndpointUriBuilder;
import net.orekyuu.nilou.EndpointUriBuilder;
import net.orekyuu.nilou.PathSegment;
import net.orekyuu.nilou.endpoint.Endpoint;
import net.orekyuu.nilou.endpoint.HandlerMethod;
import net.orekyuu.nilou.endpoint.QueryParam;

import javax.lang.model.element.Modifier;
import java.util.List;

public class HandlerClassMethod implements GenerateCode, Comparable<HandlerClassMethod> {

    final HandlerMethod handlerMethod;
    final Endpoint endpoint;

    public HandlerClassMethod(HandlerMethod method) {
        this.handlerMethod = method;
        this.endpoint = handlerMethod.endpoint();
    }

    @Override
    public void generate(TypeSpec.Builder typeSpec, Context context) {
        List<ParameterSpec> pathParameters = endpoint.segments().stream()
                .filter(it -> it instanceof PathSegment.Variable).map(it -> (PathSegment.Variable) it)
                .map(v -> ParameterSpec.builder(ClassName.get(String.class), v.name()).build())
                .toList();
        List<ParameterSpec> requiredQueryParameter = endpoint.params().stream()
                .filter(QueryParam::required)
                .map(it -> ParameterSpec.builder(ClassName.get(String.class), it.name()).build())
                .toList();

        MethodSpec methodSpec = MethodSpec.methodBuilder(handlerMethod.methodElement().getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ClassName.get(EndpointUriBuilder.class))
                .addCode(body())
                .addParameters(pathParameters)
                .addParameters(requiredQueryParameter)
                .addJavadoc(comment())
                .build();

        typeSpec.addMethod(methodSpec);
    }

    private CodeBlock segments() {
        return CodeBlock.of("$T<$T> segments = $T.of($L)",
                List.class, PathSegment.class, List.class,
                endpoint.segments().stream().map(this::pathSegmentStatement).collect(CodeBlock.joining(", ")));
    }

    private CodeBlock pathSegmentStatement(PathSegment segment) {
        if (segment instanceof PathSegment.Literal literal) {
            return CodeBlock.of("$T.ofLiteral(\"$L\")", PathSegment.class, literal.pathPart());
        } else if (segment instanceof PathSegment.Variable variable) {
            return CodeBlock.of("$T.ofVariable(\"$L\", \"$L\")", PathSegment.class, variable.raw(), variable.name());
        }

        throw new IllegalArgumentException("segment = " + segment);
    }

    private CodeBlock body() {
        CodeBlock.Builder bodyBuilder = CodeBlock.builder();
        bodyBuilder.addStatement(segments());
        bodyBuilder.addStatement("$T builder = new $T(segments)", EndpointUriBuilder.class, DefaultEndpointUriBuilder.class);
        for (PathSegment segment : endpoint.segments()) {
            if (segment instanceof PathSegment.Variable v) {
                bodyBuilder.addStatement("builder.pathParam(\"$L\", $L)", v.name(), v.name());
            }
        }

        endpoint.params().stream()
                .filter(QueryParam::required)
                .forEachOrdered(it -> {
                    bodyBuilder.addStatement("builder.queryParam(\"$L\", $L)", it.name(), it.name());
                });
        bodyBuilder.addStatement("return builder");
        return bodyBuilder.build();
    }

    private CodeBlock comment() {
        CodeBlock.Builder builder = CodeBlock.builder();
        for (String httpMethod : handlerMethod.httpMethod()) {
            builder.add("$L $L <br>\n", httpMethod, handlerMethod.endpoint().rawPathString());
        }
        builder.add("\n");
        builder.add("@see $T#$L", handlerMethod.handlerType(), handlerMethod.methodElement().getSimpleName());
        return builder.build();
    }

    @Override
    public void generate(List<JavaFile> files, Context context) {

    }

    @Override
    public int compareTo(HandlerClassMethod o) {
        return handlerMethod.compareTo(o.handlerMethod);
    }
}
