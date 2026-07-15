package com.pluxurydolo.geo.service;

import com.graphhopper.ResponsePath;
import com.pluxurydolo.geo.dto.GeoRouteData;
import com.pluxurydolo.geo.dto.Profile;
import com.pluxurydolo.geo.dto.RoutePoint;
import com.pluxurydolo.geo.mapper.PointsMapper;
import com.pluxurydolo.geo.properties.GeoProperties;
import com.pluxurydolo.geo.util.YandexMapsUrlBuilder;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.Locale.ROOT;

public class GeoRouteService {
    private final Function<String, GeoRouteData> geoRouteDataProvider;
    private final RoutingService routingService;
    private final PointsMapper pointsMapper;
    private final YandexMapsUrlBuilder yandexMapsUrlBuilder;
    private final GeoProperties geoProperties;

    public GeoRouteService(
        Function<String, GeoRouteData> geoRouteDataProvider,
        RoutingService routingService,
        PointsMapper pointsMapper,
        YandexMapsUrlBuilder yandexMapsUrlBuilder,
        GeoProperties geoProperties
    ) {
        this.geoRouteDataProvider = geoRouteDataProvider;
        this.routingService = routingService;
        this.pointsMapper = pointsMapper;
        this.yandexMapsUrlBuilder = yandexMapsUrlBuilder;
        this.geoProperties = geoProperties;
    }

    public String buildMap(String id, Model model) {
        GeoRouteData geoRouteData = geoRouteDataProvider.apply(id);

        String user = geoRouteData.user();
        Profile profile = geoRouteData.profile();
        String profileName = profile.name().toLowerCase(ROOT);
        List<RoutePoint> points = geoRouteData.points();
        int pointCount = points.size();

        String mapTitle = geoProperties.routeMap().title();
        String yandexUrl = yandexMapsUrlBuilder.build(geoRouteData);

        model.addAttribute("mapTitle", mapTitle);
        model.addAttribute("geoRouteData", geoRouteData);
        model.addAttribute("id", id);
        model.addAttribute("user", user);
        model.addAttribute("profileName", profileName);
        model.addAttribute("pointCount", pointCount);
        model.addAttribute("yandexUrl", yandexUrl);

        return "route";
    }

    public Map<String, Object> buildRoute(String id, String profileName) {
        GeoRouteData geoRouteData = geoRouteDataProvider.apply(id);
        List<RoutePoint> waypoints = geoRouteData.points();
        Profile profile = Profile.fromType(profileName);

        ResponsePath responsePath = routingService.getRouteWithWaypoints(waypoints, profile);
        List<double[]> coordinates = pointsMapper.mapToCoordinates(responsePath);

        double distance = responsePath.getDistance();
        double distanceKm = distance / 1000;
        long timeMs = responsePath.getTime();
        long timeMinutes = timeMs / 60000;
        int pointCount = waypoints.size();
        int routePointCount = coordinates.size();

        Map<String, Object> result = new ConcurrentHashMap<>();
        result.put("profile", profileName);
        result.put("distance", distance);
        result.put("distanceKm", distanceKm);
        result.put("time", timeMs);
        result.put("timeMinutes", timeMinutes);
        result.put("points", coordinates);
        result.put("pointCount", pointCount);
        result.put("routePointCount", routePointCount);

        return result;
    }
}
