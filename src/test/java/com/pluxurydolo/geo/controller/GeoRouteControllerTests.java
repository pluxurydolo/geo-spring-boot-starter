package com.pluxurydolo.geo.controller;

import com.pluxurydolo.geo.service.GeoRouteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeoRouteControllerTests {

    @Mock
    private GeoRouteService geoRouteService;

    @Mock
    private Model model;

    @InjectMocks
    private GeoRouteController geoRouteController;

    @Test
    void testBuildMap() {
        when(geoRouteService.buildMap(anyString(), any()))
            .thenReturn("map");

        String result = geoRouteController.buildMap("id", model);

        assertThat(result)
            .isEqualTo("map");
    }

    @Test
    void testBuildRoute() {
        when(geoRouteService.buildRoute(anyString(), any()))
            .thenReturn(result());

        Map<String, Object> result = geoRouteController.buildRoute("id", "profileName");

        assertThat(result)
            .isEqualTo(result());
    }

    private static Map<String, Object> result() {
        return Map.of("key", "value");
    }
}
