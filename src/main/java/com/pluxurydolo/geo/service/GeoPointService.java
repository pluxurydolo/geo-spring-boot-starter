package com.pluxurydolo.geo.service;

import com.pluxurydolo.geo.dto.GeoPoint;
import com.pluxurydolo.geo.dto.GeoPointData;
import com.pluxurydolo.geo.properties.GeoProperties;
import org.springframework.ui.Model;

import java.util.List;
import java.util.function.Function;

public class GeoPointService {
    private final Function<String, GeoPointData> geoPointDataProvider;
    private final GeoProperties geoProperties;

    public GeoPointService(Function<String, GeoPointData> geoPointDataProvider, GeoProperties geoProperties) {
        this.geoPointDataProvider = geoPointDataProvider;
        this.geoProperties = geoProperties;
    }

    public String buildMap(String id, Model model) {
        GeoPointData geoPointData = geoPointDataProvider.apply(id);
        String user = geoPointData.user();
        List<GeoPoint> points = geoPointData.points();
        int pointCount = points.size();
        String mapTitle = geoProperties.pointMap().title();

        model.addAttribute("mapTitle", mapTitle);
        model.addAttribute("points", points);
        model.addAttribute("user", user);
        model.addAttribute("pointCount", pointCount);

        return "point";
    }
}
