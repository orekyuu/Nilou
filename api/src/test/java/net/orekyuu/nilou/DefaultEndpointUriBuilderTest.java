package net.orekyuu.nilou;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultEndpointUriBuilderTest {

  @Test
  void test() {
    String id = "orekyuu";
    DefaultEndpointUriBuilder builder = new DefaultEndpointUriBuilder(List.of(PathSegment.ofLiteral("users"), PathSegment.ofVariable("{id}", "id")));
    builder.pathParam("id", id);
    assertThat(builder.toUri()).hasPath("/users/orekyuu");
  }
}