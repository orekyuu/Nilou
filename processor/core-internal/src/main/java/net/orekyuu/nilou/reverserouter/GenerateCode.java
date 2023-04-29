package net.orekyuu.nilou.reverserouter;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface GenerateCode {

    class Context {
        Map<ClassName, ClassName> renamedMap = new HashMap<>();
    }
    void generate(TypeSpec.Builder typeSpec, Context context);

    void generate(List<JavaFile> files, Context context);
}
