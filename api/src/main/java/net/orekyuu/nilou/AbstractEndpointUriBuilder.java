package net.orekyuu.nilou;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractEndpointUriBuilder<T extends AbstractEndpointUriBuilder<T>> implements EndpointUriBuilder<T> {

  // Note: query paramの順序保持のためにLinkedHashMapを利用する
  private final Map<String, List<String>> queryParams = new LinkedHashMap<>();
  private URI base;
  private final List<PathSegment> pathSegments;

  protected AbstractEndpointUriBuilder(List<PathSegment> pathSegments) {
    this(URI.create(""), pathSegments);
  }

  protected AbstractEndpointUriBuilder(URI base, List<PathSegment> pathSegments) {
    this.base = base;
    this.pathSegments = pathSegments;
  }

  @Override
  public T baseUri(URI base) {
    this.base = base;
    return self();
  }

  @Override
  public URI toUri() {
    StringBuilder builder = new StringBuilder(base.toString());
    if (!base.toString().endsWith("/")) {
      builder.append("/");
    }
    String pathPart = pathSegments.stream().map(PathSegment::pathPart).collect(Collectors.joining("/"));
    builder.append(pathPart);

    if (!queryParams.isEmpty()) {
      String queryPart = queryParams.entrySet().stream()
              .flatMap(keyValue -> keyValue.getValue().stream().map(v -> keyValue.getKey() + "=" + v))
              .collect(Collectors.joining("&", "?", ""));
      builder.append(queryPart);
    }

    return URI.create(builder.toString());
  }

  @Override
  @SuppressWarnings("unchecked cast")
  public T self() {
    return (T) this;
  }

  @Override
  public T pathParam(String name, String value) {
    boolean updated = false;
    for (PathSegment pathSegment : pathSegments) {
      if (pathSegment instanceof PathSegment.Variable v && Objects.equals(v.name, name)) {
        v.value = value;
        updated = true;
      }
    }

    if (!updated) {
      throw new IllegalArgumentException("PathParam not found: " + name);
    }
    return self();
  }

  @Override
  public T queryParam(String name, String value) {
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
  public T queryParam(String name, String... values) {
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
