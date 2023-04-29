package net.orekyuu.nilou.reverserouter;

import com.squareup.javapoet.*;
import net.orekyuu.nilou.DefaultEndpointUriBuilder;
import net.orekyuu.nilou.EndpointUriBuilder;
import net.orekyuu.nilou.PathSegment;
import net.orekyuu.nilou.endpoint.Endpoint;
import net.orekyuu.nilou.endpoint.HandlerMethod;

import javax.lang.model.element.Modifier;
import java.util.List;

public class HandlerClassMethod implements GenerateCode, Comparable<HandlerClassMethod> {

    final HandlerMethod handlerMethod;

    public HandlerClassMethod(HandlerMethod method) {
        this.handlerMethod = method;
    }

    @Override
    public void generate(TypeSpec.Builder typeSpec, Context context) {
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

        MethodSpec methodSpec = MethodSpec.methodBuilder(handlerMethod.methodElement().getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ClassName.get(EndpointUriBuilder.class))
                .addCode(bodyBuilder.build())
                .addParameters(parameters)
                .addJavadoc(comment())
                .build();

        typeSpec.addMethod(methodSpec);
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
