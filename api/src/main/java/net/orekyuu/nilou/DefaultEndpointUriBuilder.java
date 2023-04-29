package net.orekyuu.nilou;

import java.net.URI;
import java.util.List;

public class DefaultEndpointUriBuilder extends AbstractEndpointUriBuilder<DefaultEndpointUriBuilder> {

  protected DefaultEndpointUriBuilder(List<PathSegment> pathSegments) {
    super(pathSegments);
  }

  protected DefaultEndpointUriBuilder(URI base, List<PathSegment> pathSegments) {
    super(base, pathSegments);
  }
}
