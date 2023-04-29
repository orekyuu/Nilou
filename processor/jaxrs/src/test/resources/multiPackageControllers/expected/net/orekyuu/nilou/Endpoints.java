package net.orekyuu.nilou;

import java.lang.String;
import java.util.List;

public final class Endpoints {
    public static final class FugaController {
        public static EndpointUriBuilder all() {
            List<PathSegment> segments = List.of(PathSegment.ofLiteral("fuga"));
            EndpointUriBuilder builder = new DefaultEndpointUriBuilder(segments);
            return builder;
        }
    }

    public static final class hoge_HogeController {
        public static EndpointUriBuilder all() {
            List<PathSegment> segments = List.of(PathSegment.ofLiteral("hoge"));
            EndpointUriBuilder builder = new DefaultEndpointUriBuilder(segments);
            return builder;
        }

        public static EndpointUriBuilder get(String id) {
            List<PathSegment> segments = List.of(PathSegment.ofLiteral("hoge"), PathSegment.ofVariable("{id}", "id"));
            EndpointUriBuilder builder = new DefaultEndpointUriBuilder(segments);
            builder.pathParam("id", id);
            return builder;
        }
    }
}