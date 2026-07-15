package com.pluxurydolo.geo.dto;

import java.util.List;

public record GeoPointData(
    String user,
    List<GeoPoint> points
) {
}
