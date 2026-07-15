package com.pluxurydolo.geo.controller;

import com.pluxurydolo.geo.service.GeoPointService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GeoPointController {
    private final GeoPointService geoPointService;

    public GeoPointController(GeoPointService geoPointService) {
        this.geoPointService = geoPointService;
    }

    @GetMapping("/geo/point")
    public String buildMap(@RequestParam String id, Model model) {
        return geoPointService.buildMap(id, model);
    }
}
