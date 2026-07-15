package com.pluxurydolo.geo.service;

import com.graphhopper.ResponsePath;
import com.pluxurydolo.geo.dto.GeoRouteData;
import com.pluxurydolo.geo.dto.RoutePoint;
import com.pluxurydolo.geo.mapper.PointsMapper;
import com.pluxurydolo.geo.properties.GeoProperties;
import com.pluxurydolo.geo.util.YandexMapsUrlBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.pluxurydolo.geo.dto.Profile.FOOT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeoRouteServiceTests {

    @Mock
    private Function<String, GeoRouteData> geoRouteDataProvider;

    @Mock
    private RoutingService routingService;

    @Mock
    private PointsMapper pointsMapper;

    @Mock
    private YandexMapsUrlBuilder yandexMapsUrlBuilder;

    @Mock
    private GeoProperties geoProperties;

    @Mock
    private Model model;

    @InjectMocks
    private GeoRouteService geoRouteService;

    @Test
    void testBuildMap() {
        when(geoRouteDataProvider.apply(anyString()))
            .thenReturn(geoRouteData());
        when(geoProperties.routeMap())
            .thenReturn(mapProperties());
        when(yandexMapsUrlBuilder.build(any()))
            .thenReturn("");
        when(model.addAttribute(anyString(), any()))
            .thenReturn(model);

        String result = geoRouteService.buildMap("id", model);

        assertThat(result)
            .isEqualTo("route");
    }

    @Test
    void testBuildRoute() {
        when(geoRouteDataProvider.apply(anyString()))
            .thenReturn(geoRouteData());
        when(routingService.getRouteWithWaypoints(anyList(), any()))
            .thenReturn(responsePath());
        when(pointsMapper.mapToCoordinates(any()))
            .thenReturn(coordinates());

        Map<String, Object> result = geoRouteService.buildRoute("id", "foot");

        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(result());
    }

    private static GeoRouteData geoRouteData() {
        return new GeoRouteData("user", routePoints(), FOOT, "label");
    }

    private static List<RoutePoint> routePoints() {
        return List.of(
            new RoutePoint(0.0, 2.0, "label1"),
            new RoutePoint(45.0, 22.5, "label2"),
            new RoutePoint(67.5, 90.0, "label3")
        );
    }

    private static GeoProperties.MapProperties mapProperties() {
        return new GeoProperties.MapProperties("title");
    }

    private static ResponsePath responsePath() {
        ResponsePath responsePath = new ResponsePath();
        responsePath.setDistance(10_000);
        responsePath.setTime(600_000);
        return responsePath;
    }

    private static List<double[]> coordinates() {
        return List.of(
            new double[]{2.0, 0.0},
            new double[]{22.5, 45.0},
            new double[]{90.0, 67.5}
        );
    }

    private static Map<String, Object> result() {
        List<double[]> points = List.of(
            new double[]{2.0, 0.0},
            new double[]{22.5, 45.0},
            new double[]{90.0, 67.5}
        );

        return Map.of(
            "profile", "foot",
            "distance", 10000.0,
            "distanceKm", 10.0,
            "time", 600000L,
            "timeMinutes", 10L,
            "points", points,
            "pointCount", 3,
            "routePointCount", 3
        );
    }
}
