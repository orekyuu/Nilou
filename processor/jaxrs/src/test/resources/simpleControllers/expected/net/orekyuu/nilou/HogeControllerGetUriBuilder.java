package net.orekyuu.nilou;

import java.util.List;

public final class HogeControllerGetUriBuilder extends AbstractEndpointUriBuilder {
    HogeControllerGetUriBuilder(String id) {
        super(List.of(PathSegment.ofLiteral("hoge")));
    }
}