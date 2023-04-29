package net.orekyuu.nilou.reverserouter;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.util.List;


public interface GenerateCode {

    void generate(TypeSpec.Builder typeSpec);

    void generate(List<JavaFile> files);
}
