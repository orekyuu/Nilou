package net.orekyuu.nilou;

import java.lang.String;
import java.util.List;

public final class Endpoints {
    public static final class FugaController {
        /**
         * TODO /fuga <br>
         *
         * @see test.FugaController#all
         */
        public static EndpointUriBuilder all(String mode) {
            List<PathSegment> segments = List.of(PathSegment.ofLiteral("fuga"));
            EndpointUriBuilder builder = new DefaultEndpointUriBuilder(segments);
            builder.queryParam("mode", mode);
            return builder;
        }
    }

    public static final class hoge_HogeController {
        /**
         * TODO /hoge <br>
         *
         * @see test.hoge.HogeController#all
         */
        public static EndpointUriBuilder all() {
            List<PathSegment> segments = List.of(PathSegment.ofLiteral("hoge"));
            EndpointUriBuilder builder = new DefaultEndpointUriBuilder(segments);
            return builder;
        }

        /**
         * TODO /hoge/{id} <br>
         *
         * @see test.hoge.HogeController#get
         */
        public static EndpointUriBuilder get(String id) {
            List<PathSegment> segments = List.of(PathSegment.ofLiteral("hoge"), PathSegment.ofVariable("{id}", "id"));
            EndpointUriBuilder builder = new DefaultEndpointUriBuilder(segments);
            builder.pathParam("id", id);
            return builder;
        }
    }
}