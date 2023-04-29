package net.orekyuu.nilou.endpoint;

import com.squareup.javapoet.TypeName;

public record QueryParam(String name, TypeName type, boolean required) {

}
