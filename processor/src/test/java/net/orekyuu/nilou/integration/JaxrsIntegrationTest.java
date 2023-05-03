package net.orekyuu.nilou.integration;


import net.orekyuu.nilou.testing.AnnotationProcessorTesting;
import org.junit.jupiter.api.Test;

public class JaxrsIntegrationTest extends AnnotationProcessorTesting {

  @Test
  void testSimpleControllers() {
    compile("simpleControllers");
  }

  @Test
  void multiPackageControllers() {
    compile("multiPackageControllers");
  }
}