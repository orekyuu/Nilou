package net.orekyuu.nilou.reverserouter;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import net.orekyuu.nilou.NamingUtils;
import net.orekyuu.nilou.endpoint.HandlerMethod;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Endpoints implements GenerateCode {

    private static final ClassName CLASS_NAME = ClassName.get("net.orekyuu.nilou", "Endpoints");
    private final List<HandlerClass> handlerClasses = new ArrayList<>();

    public void addEndpoint(TypeElement element, List<HandlerMethod> methods) {
        HandlerClass handlerClass = new HandlerClass(element);
        methods.forEach(handlerClass::addMethod);
        handlerClasses.add(handlerClass);
    }

    public JavaFile endpointsFile() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(CLASS_NAME);
        generate(builder, new Context());
        return JavaFile.builder(CLASS_NAME.packageName(), builder.build()).build();
    }

    @Override
    public void generate(TypeSpec.Builder typeSpec, Context context) {
        context.renamedMap = createRenamedMap();

        typeSpec.addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        handlerClasses.sort(Comparator.naturalOrder());
        for (HandlerClass handlerClass : handlerClasses) {
            handlerClass.generate(typeSpec, context);
        }
    }


    @Override
    public void generate(List<JavaFile> files, Context context) {
        context.renamedMap = createRenamedMap();
        for (HandlerClass handlerClass : handlerClasses) {
            handlerClass.generate(files, context);
        }
    }

    private Map<ClassName, ClassName> createRenamedMap() {
        List<ClassName> originalNames = handlerClasses.stream()
                .map(it -> ClassName.get(it.handlerClass))
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
