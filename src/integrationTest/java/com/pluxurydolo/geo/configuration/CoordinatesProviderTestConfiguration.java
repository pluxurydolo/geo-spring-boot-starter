package com.pluxurydolo.geo.configuration;

import com.pluxurydolo.geo.dto.GeoPoint;
import com.pluxurydolo.geo.dto.GeoPointData;
import com.pluxurydolo.geo.dto.GeoRouteData;
import com.pluxurydolo.geo.dto.RoutePoint;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.function.Function;

import static com.pluxurydolo.geo.dto.Profile.FOOT;

@TestConfiguration
public class CoordinatesProviderTestConfiguration {

    @Bean
    public Function<String, GeoPointData> geoPointCoordinatesProvider() {
        return _ -> geoPointData();
    }

    @Bean
    public Function<String, GeoRouteData> geoRouteCoordinatesProvider() {
        return _ -> geoRouteData();
    }

    private static GeoPointData geoPointData() {
        return new GeoPointData(
            "user",
            List.of(
                new GeoPoint("1", "Дом", "Здесь живет пользователь", 55.7558, 37.6173),
                new GeoPoint("2", "Кофейня", "Кофе с собой", 55.7580, 37.6210),
                new GeoPoint("3", "Супермаркет", "Продуктовый", 55.7500, 37.6300)
            )
        );
    }

    private static GeoRouteData geoRouteData() {
        return new GeoRouteData(
            "user",
            List.of(
                new RoutePoint(55.751244, 37.618423, "Старт"),
                new RoutePoint(55.7580, 37.6210, "Кофейня"),
                new RoutePoint(55.7500, 37.6300, "Супермаркет"),
                new RoutePoint(55.7450, 37.6400, "Парк"),
                new RoutePoint(55.755831, 37.617673, "Финиш")
            ),
            FOOT,
            "Route-123"
        );
    }
}
