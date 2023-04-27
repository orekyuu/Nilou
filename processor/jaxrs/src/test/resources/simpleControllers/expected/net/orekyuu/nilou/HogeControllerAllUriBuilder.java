package net.orekyuu.nilou;

import java.util.List;

public final class HogeControllerAllUriBuilder extends AbstractEndpointUriBuilder {
    HogeControllerAllUriBuilder() {
        super(List.of(PathSegment.ofLiteral("hoge"), PathSegment.ofVariable("id", "id")));
    }
}