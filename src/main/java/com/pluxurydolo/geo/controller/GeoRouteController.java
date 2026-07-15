package com.pluxurydolo.geo.controller;

import com.pluxurydolo.geo.service.GeoRouteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class GeoRouteController {
    private final GeoRouteService geoRouteService;

    public GeoRouteController(GeoRouteService geoRouteService) {
        this.geoRouteService = geoRouteService;
    }

    @GetMapping("/geo/route")
    public String buildMap(@RequestParam String id, Model model) {
        return geoRouteService.buildMap(id, model);
    }

    @GetMapping("/geo/route/waypoints")
    @ResponseBody
    public Map<String, Object> buildRoute(
        @RequestParam String id,
        @RequestParam String profileName
    ) {
        return geoRouteService.buildRoute(id, profileName);
    }
}
