package net.orekyuu.nilou;

import java.net.URI;

public interface EndpointUriBuilder<T extends EndpointUriBuilder<T>> {

  URI toUri();

  T self();

  T pathParam(String name, String value);

  T queryParam(String name, String value);

  T queryParam(String name, String... values);
}
