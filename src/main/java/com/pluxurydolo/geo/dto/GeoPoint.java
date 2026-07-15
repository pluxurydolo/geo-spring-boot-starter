package com.pluxurydolo.geo.dto;

public record GeoPoint(
    String id,
    String title,
    String description,
    double latitude,
    double longitude
) {
}
