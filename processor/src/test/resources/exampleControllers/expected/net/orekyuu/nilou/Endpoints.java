package net.orekyuu.nilou;

import java.util.List;

public final class Endpoints {
    public static final class ExampleResource {
        /**
         * GET /hello <br>
         *
         * @see com.example.ExampleResource#hello
         */
        public static EndpointUriBuilder hello() {
            List<PathSegment> segments = List.of(PathSegment.ofLiteral("hello"));
            EndpointUriBuilder builder = new DefaultEndpointUriBuilder(segments);
            return builder;
        }
    }
}