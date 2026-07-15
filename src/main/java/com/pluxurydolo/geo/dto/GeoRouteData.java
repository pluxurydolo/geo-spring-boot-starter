package com.pluxurydolo.geo.dto;

import java.util.List;

public record GeoRouteData(
    String user,
    List<RoutePoint> points,
    Profile profile,
    String label
) {
}
