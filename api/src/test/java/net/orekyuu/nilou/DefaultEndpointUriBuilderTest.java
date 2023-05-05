package net.orekyuu.nilou;


import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultEndpointUriBuilderTest {

  @Test
  void testPathVariable() {
    String id = "orekyuu";
    DefaultEndpointUriBuilder builder = new DefaultEndpointUriBuilder(List.of(PathSegment.ofLiteral("users"), PathSegment.ofVariable("{id}", "id")));
    builder.pathParam("id", id);
    assertThat(builder.toUri()).isEqualTo(URI.create("/users/orekyuu"));
  }

  @Test
  void testDuplicatedPathVariable() {
    String id = "orekyuu";
    DefaultEndpointUriBuilder builder = new DefaultEndpointUriBuilder(List.of(PathSegment.ofLiteral("users"), PathSegment.ofVariable("{id}", "id"), PathSegment.ofLiteral("hoge"), PathSegment.ofVariable("{id}", "id")));
    builder.pathParam("id", id);
    assertThat(builder.toUri()).isEqualTo(URI.create("/users/orekyuu/hoge/orekyuu"));
  }

  @Test
  void testMultiPathVariable() {
    String user1Id = "user1";
    String user2Id = "user2";
    DefaultEndpointUriBuilder builder = new DefaultEndpointUriBuilder(List.of(
            PathSegment.ofLiteral("users"),
            PathSegment.ofVariable("{id1}", "id1"),
            PathSegment.ofLiteral("hoge"),
            PathSegment.ofVariable("{id2}", "id2")));
    builder.pathParam("id1", user1Id);
    builder.pathParam("id2", user2Id);
    assertThat(builder.toUri()).isEqualTo(URI.create("/users/user1/hoge/user2"));
  }

  @Test
  void testQueryParam() {
    String id = "orekyuu";
    DefaultEndpointUriBuilder builder = new DefaultEndpointUriBuilder(List.of(PathSegment.ofLiteral("users")));
    builder.queryParam("id", id);
    assertThat(builder.toUri())
            .isEqualTo(URI.create("/users?id=orekyuu"));
  }

  @Test
  void testListQueryParam() {
    String user1Id = "user1";
    String user2Id = "user2";
    DefaultEndpointUriBuilder builder = new DefaultEndpointUriBuilder(List.of(PathSegment.ofLiteral("users")));
    builder.queryParam("id", user1Id, user2Id);
    assertThat(builder.toUri())
            .isEqualTo(URI.create("/users?id=user1&id=user2"));
  }

  @Test
  void testListQueryParamAnyCall() {
    String user1Id = "user1";
    String user2Id = "user2";
    DefaultEndpointUriBuilder builder = new DefaultEndpointUriBuilder(List.of(PathSegment.ofLiteral("users")));
    builder.queryParam("id", user1Id);
    builder.queryParam("id", user2Id);
    assertThat(builder.toUri())
            .isEqualTo(URI.create("/users?id=user1&id=user2"));
  }

  @Test
  void testMultiQueryParam() {
    String user1Id = "user1";
    String user2Id = "user2";
    DefaultEndpointUriBuilder builder = new DefaultEndpointUriBuilder(List.of(PathSegment.ofLiteral("users")));
    builder.queryParam("id1", user1Id);
    builder.queryParam("id2", user2Id);
    assertThat(builder.toUri())
            .isEqualTo(URI.create("/users?id1=user1&id2=user2"));
  }

  @Test
  void testBaseUri() {
    DefaultEndpointUriBuilder builder = new DefaultEndpointUriBuilder(List.of(PathSegment.ofLiteral("users"), PathSegment.ofVariable("{id}", "id")));
    builder
            .pathParam("id", "orekyuu")
            .queryParam("lang", "ja")
            .baseUri(URI.create("https://orekyuu.net"));

    assertThat(builder.toUri())
            .isEqualTo(URI.create("https://orekyuu.net/users/orekyuu?lang=ja"));
  }

  @Test
  void testInvalidPathParam() {
    DefaultEndpointUriBuilder builder = new DefaultEndpointUriBuilder(List.of(PathSegment.ofLiteral("users")));
    assertThatThrownBy(() -> builder.pathParam("invalid", "value"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("PathParam not found: invalid");
  }
}