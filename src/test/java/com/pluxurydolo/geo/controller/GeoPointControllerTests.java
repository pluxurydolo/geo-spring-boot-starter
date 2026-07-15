package com.pluxurydolo.geo.controller;

import com.pluxurydolo.geo.service.GeoPointService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeoPointControllerTests {

    @Mock
    private GeoPointService geoPointService;

    @Mock
    private Model model;

    @InjectMocks
    private GeoPointController geoPointController;

    @Test
    void testBuildMap() {
        when(geoPointService.buildMap(anyString(), any()))
            .thenReturn("map");

        String result = geoPointController.buildMap("id", model);

        assertThat(result)
            .isEqualTo("map");
    }
}
