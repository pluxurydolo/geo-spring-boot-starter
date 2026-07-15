package com.pluxurydolo.geo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;

@ConfigurationProperties(prefix = "geo")
public record GeoProperties(
    @Name("map.point") MapProperties pointMap,
    @Name("map.route") MapProperties routeMap
) {
    public record MapProperties(
        String title
    ) {
    }
}
