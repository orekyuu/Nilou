package net.orekyuu.nilou.endpoint;

import net.orekyuu.nilou.PathSegment;

import java.util.List;

public record Endpoint(List<PathSegment> segments, List<QueryParam> params) {

}
