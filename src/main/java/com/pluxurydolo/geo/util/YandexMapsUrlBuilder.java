package com.pluxurydolo.geo.util;

import com.pluxurydolo.geo.dto.GeoRouteData;

import static java.util.stream.Collectors.joining;

public class YandexMapsUrlBuilder {
    public String build(GeoRouteData geoRouteData) {
        String rtt = geoRouteData.profile().getRtt();

        String rtext = geoRouteData.points()
            .stream()
            .map(point -> point.latitude() + "," + point.longitude())
            .collect(joining("~"));

        return String.format("https://yandex.ru/maps/?rtext=%s&rtt=%s", rtext, rtt);
    }
}
