package com.pluxurydolo.geo.util;

import com.pluxurydolo.geo.dto.GeoRouteData;
import com.pluxurydolo.geo.dto.RoutePoint;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.pluxurydolo.geo.dto.Profile.FOOT;
import static org.assertj.core.api.Assertions.assertThat;

class YandexMapsUrlBuilderTests {
    private static final YandexMapsUrlBuilder BUILDER = new YandexMapsUrlBuilder();

    @Test
    void testBuild() {
        String result = BUILDER.build(geoRouteData());

        assertThat(result)
            .isEqualTo("https://yandex.ru/maps/?rtext=0.0,2.0~45.0,22.5~67.5,90.0&rtt=pd");
    }

    private static GeoRouteData geoRouteData() {
        return new GeoRouteData("user", routePoints(), FOOT, "route1");
    }

    private static List<RoutePoint> routePoints() {
        return List.of(
            new RoutePoint(0.0, 2.0, "label1"),
            new RoutePoint(45.0, 22.5, "label2"),
            new RoutePoint(67.5, 90.0, "label3")
        );
    }
}
