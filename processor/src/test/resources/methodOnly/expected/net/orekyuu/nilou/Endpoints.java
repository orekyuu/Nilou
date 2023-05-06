package net.orekyuu.nilou;

import java.lang.String;
import java.util.List;

public final class Endpoints {
    public static final class MethodOnlyController {
        /**
         * DELETE /method/{id} <br>
         *
         * @see test.MethodOnlyController#delete
         */
        public static EndpointUriBuilder delete(String id) {
            List<PathSegment> segments = List.of(PathSegment.ofLiteral("method"), PathSegment.ofVariable("{id}", "id"));
            EndpointUriBuilder builder = new DefaultEndpointUriBuilder(segments);
            builder.pathParam("id", id);
            return builder;
        }

        /**
         * GET /method/{id} <br>
         *
         * @see test.MethodOnlyController#get
         */
        public static EndpointUriBuilder get(String id) {
            List<PathSegment> segments = List.of(PathSegment.ofLiteral("method"), PathSegment.ofVariable("{id}", "id"));
            EndpointUriBuilder builder = new DefaultEndpointUriBuilder(segments);
            builder.pathParam("id", id);
            return builder;
        }

        /**
         * HEAD /method/{id} <br>
         *
         * @see test.MethodOnlyController#head
         */
        public static EndpointUriBuilder head(String id) {
            List<PathSegment> segments = List.of(PathSegment.ofLiteral("method"), PathSegment.ofVariable("{id}", "id"));
            EndpointUriBuilder builder = new DefaultEndpointUriBuilder(segments);
            builder.pathParam("id", id);
            return builder;
        }

        /**
         * OPTIONS /method/{id} <br>
         *
         * @see test.MethodOnlyController#options
         */
        public static EndpointUriBuilder options(String id) {
            List<PathSegment> segments = List.of(PathSegment.ofLiteral("method"), PathSegment.ofVariable("{id}", "id"));
            EndpointUriBuilder builder = new DefaultEndpointUriBuilder(segments);
            builder.pathParam("id", id);
            return builder;
        }

        /**
         * PATCH /method/{id} <br>
         *
         * @see test.MethodOnlyController#patch
         */
        public static EndpointUriBuilder patch(String id) {
            List<PathSegment> segments = List.of(PathSegment.ofLiteral("method"), PathSegment.ofVariable("{id}", "id"));
            EndpointUriBuilder builder = new DefaultEndpointUriBuilder(segments);
            builder.pathParam("id", id);
            return builder;
        }

        /**
         * POST /method/{id} <br>
         *
         * @see test.MethodOnlyController#post
         */
        public static EndpointUriBuilder post(String id) {
            List<PathSegment> segments = List.of(PathSegment.ofLiteral("method"), PathSegment.ofVariable("{id}", "id"));
            EndpointUriBuilder builder = new DefaultEndpointUriBuilder(segments);
            builder.pathParam("id", id);
            return builder;
        }

        /**
         * PUT /method/{id} <br>
         *
         * @see test.MethodOnlyController#put
         */
        public static EndpointUriBuilder put(String id) {
            List<PathSegment> segments = List.of(PathSegment.ofLiteral("method"), PathSegment.ofVariable("{id}", "id"));
            EndpointUriBuilder builder = new DefaultEndpointUriBuilder(segments);
            builder.pathParam("id", id);
            return builder;
        }
    }

    public static final class MultiMethodController {
        /**
         * GET /method/{id} <br>
         * POST /method/{id} <br>
         *
         * @see test.MultiMethodController#getAndPost
         */
        public static EndpointUriBuilder getAndPost(String id) {
            List<PathSegment> segments = List.of(PathSegment.ofLiteral("method"), PathSegment.ofVariable("{id}", "id"));
            EndpointUriBuilder builder = new DefaultEndpointUriBuilder(segments);
            builder.pathParam("id", id);
            return builder;
        }
    }
}