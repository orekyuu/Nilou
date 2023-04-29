package net.orekyuu.nilou.endpoint;

import net.orekyuu.nilou.PathSegment;

import java.util.List;

public record Endpoint(List<PathSegment> segments, List<QueryParam> params) {

  public Endpoint {
    segments = segments.stream()
            .filter(it -> !(it instanceof PathSegment.Literal literal && literal.pathPart().isEmpty()))
            .toList();
  }
}
