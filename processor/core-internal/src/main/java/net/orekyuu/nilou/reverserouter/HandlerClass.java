package net.orekyuu.nilou.reverserouter;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import net.orekyuu.nilou.endpoint.HandlerMethod;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HandlerClass implements GenerateCode, Comparable<HandlerClass> {

    final TypeElement handlerClass;
    final List<HandlerClassMethod> handlerMethods = new ArrayList<>();

    public HandlerClass(TypeElement handlerClass) {
        this.handlerClass = handlerClass;
    }

    public void addMethod(HandlerMethod method) {
        handlerMethods.add(new HandlerClassMethod(method));
    }

    @Override
    public void generate(TypeSpec.Builder typeSpec, Context context) {
        ClassName className = context.renamedMap.get(ClassName.get(handlerClass));
        TypeSpec.Builder builder = TypeSpec
                .classBuilder(className.canonicalName().replace('.', '_'))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
        handlerMethods.sort(Comparator.naturalOrder());
        for (HandlerClassMethod handlerMethod : handlerMethods) {
            handlerMethod.generate(builder, context);
        }
        typeSpec.addType(builder.build());
    }

    @Override
    public void generate(List<JavaFile> files, Context context) {
        for (HandlerClassMethod method : handlerMethods) {
            method.generate(files, context);
        }
    }

    @Override
    public int compareTo(HandlerClass o) {
        return handlerClass.getQualifiedName().toString().compareTo(o.handlerClass.getQualifiedName().toString());
    }
}
