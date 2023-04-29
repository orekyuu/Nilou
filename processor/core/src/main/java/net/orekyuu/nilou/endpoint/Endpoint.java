package net.orekyuu.nilou.endpoint;

import net.orekyuu.nilou.PathSegment;

import java.util.List;
import java.util.stream.Collectors;

public record Endpoint(List<PathSegment> segments, List<QueryParam> params) {

  public Endpoint {
    segments = segments.stream()
            .filter(it -> !(it instanceof PathSegment.Literal literal && literal.pathPart().isEmpty()))
            .toList();
  }

  public String rawPathString() {
    return segments().stream().map(PathSegment::raw).collect(Collectors.joining("/", "/", ""));
  }
}
