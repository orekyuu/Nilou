package net.orekyuu.nilou;

import java.lang.String;
import java.util.List;

public final class Endpoints {
    public static final class NotNullController {
        /**
         * GET /notNull <br>
         *
         * @see test.NotNullController#all
         */
        public static EndpointUriBuilder all(String id) {
            List<PathSegment> segments = List.of(PathSegment.ofLiteral("notNull"));
            EndpointUriBuilder builder = new DefaultEndpointUriBuilder(segments);
            builder.queryParam("id", id);
            return builder;
        }

        /**
         * GET /notNull/{id} <br>
         *
         * @see test.NotNullController#all
         */
        public static EndpointUriBuilder all(String id, String mode) {
            List<PathSegment> segments = List.of(PathSegment.ofLiteral("notNull"), PathSegment.ofVariable("{id}", "id"));
            EndpointUriBuilder builder = new DefaultEndpointUriBuilder(segments);
            builder.pathParam("id", id);
            builder.queryParam("mode", mode);
            return builder;
        }
    }
}