package net.orekyuu.nilou;

sealed interface PathSegment{

  String raw();

  String pathPart();

  static Literal ofLiteral(String value) {
    return new Literal(value);
  }

  static Variable ofVariable(String raw, String name) {
    return new Variable(raw, name);
  }

  final class Literal implements PathSegment {

    final String value;

    private Literal(String value) {
      this.value = value;
    }

    @Override
    public String raw() {
      return value;
    }

    @Override
    public String pathPart() {
      return value;
    }
  }

  final class Variable implements PathSegment {
    final String raw;
    final String name;
    String value;

    private Variable(String raw, String name) {
      this.raw = raw;
      this.name = name;
    }

    public String name() {
      return name;
    }

    @Override
    public String raw() {
      return raw;
    }

    @Override
    public String pathPart() {
      return value;
    }
  }
}
