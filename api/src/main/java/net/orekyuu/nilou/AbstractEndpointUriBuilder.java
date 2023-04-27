package net.orekyuu.nilou;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractEndpointUriBuilder implements EndpointUriBuilder<AbstractEndpointUriBuilder> {

  private final Map<String, List<String>> queryParams = new HashMap<>();
  private final URI base;
  private final List<PathSegment> pathSegments;

  protected AbstractEndpointUriBuilder(List<PathSegment> pathSegments) {
    this(URI.create(""), pathSegments);
  }

  protected AbstractEndpointUriBuilder(URI base, List<PathSegment> pathSegments) {
    this.base = base;
    this.pathSegments = pathSegments;
  }

  @Override
  public URI toUri() {
    StringBuilder builder = new StringBuilder(base.toString());
    if (!base.toString().endsWith("/")) {
      builder.append("/");
    }
    String pathPart = pathSegments.stream().map(PathSegment::pathPart).collect(Collectors.joining("/"));
    builder.append(pathPart);

    String queryPart = queryParams.entrySet().stream()
            .flatMap(keyValue -> keyValue.getValue().stream().map(v -> keyValue.getKey() + "=" + v))
            .collect(Collectors.joining("&", "?", ""));
    builder.append(queryPart);

    return URI.create(builder.toString());
  }

  @Override
  public AbstractEndpointUriBuilder self() {
    return this;
  }

  @Override
  public AbstractEndpointUriBuilder pathParam(String name, String value) {
    for (PathSegment pathSegment : pathSegments) {
      if (pathSegment instanceof PathSegment.Variable v) {
        v.value = value;
      }
    }
    return self();
  }

  @Override
  public AbstractEndpointUriBuilder queryParam(String name, String value) {
    queryParams.compute(name, (key, val) -> {
      if (val == null) {
        return new ArrayList<>(List.of(value));
      }
      val.add(value);
      return val;
    });
    return self();
  }

  @Override
  public AbstractEndpointUriBuilder queryParam(String name, String... values) {
    queryParams.compute(name, (key, val) -> {
      if (val == null) {
        return new ArrayList<>(Arrays.asList(values));
      }
      val.addAll(Arrays.asList(values));
      return val;
    });
    return self();
  }
}
