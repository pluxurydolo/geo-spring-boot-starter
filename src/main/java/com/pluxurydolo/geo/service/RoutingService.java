package com.pluxurydolo.geo.service;

import com.graphhopper.GHRequest;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.util.shapes.GHPoint;
import com.pluxurydolo.geo.dto.Profile;
import com.pluxurydolo.geo.dto.RoutePoint;

import java.util.List;
import java.util.Locale;

import static java.util.Locale.ROOT;

public class RoutingService {
    private final GraphHopper graphHopper;

    public RoutingService(GraphHopper graphHopper) {
        this.graphHopper = graphHopper;
    }

    public ResponsePath getRouteWithWaypoints(List<RoutePoint> waypoints, Profile profile) {
        RoutePoint start = waypoints.getFirst();
        List<RoutePoint> intermediatePoints = waypoints.subList(1, waypoints.size() - 1);
        RoutePoint end = waypoints.getLast();

        double startLatitude = start.latitude();
        double startLongitude = start.longitude();
        double endLatitude = end.latitude();
        double endLongitude = end.longitude();
        Locale locale = Locale.forLanguageTag("ru");
        String profileName = profile.name().toLowerCase(ROOT);

        GHRequest ghRequest = new GHRequest(startLatitude, startLongitude, endLatitude, endLongitude)
            .setProfile(profileName)
            .setLocale(locale);

        intermediatePoints.stream()
            .map(point -> new GHPoint(point.latitude(), point.longitude()))
            .forEach(ghRequest::addPoint);

        return graphHopper.route(ghRequest).getBest();
    }
}
