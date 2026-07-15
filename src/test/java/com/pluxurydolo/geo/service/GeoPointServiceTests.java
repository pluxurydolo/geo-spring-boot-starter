package com.pluxurydolo.geo.service;

import com.pluxurydolo.geo.dto.GeoPoint;
import com.pluxurydolo.geo.dto.GeoPointData;
import com.pluxurydolo.geo.properties.GeoProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeoPointServiceTests {

    @Mock
    private Function<String, GeoPointData> geoPointDataProvider;

    @Mock
    private GeoProperties geoProperties;

    @Mock
    private Model model;

    @InjectMocks
    private GeoPointService geoPointService;

    @Test
    void testBuildMap() {
        when(geoPointDataProvider.apply(anyString()))
            .thenReturn(geoPointData());
        when(geoProperties.pointMap())
            .thenReturn(mapProperties());
        when(model.addAttribute(anyString(), any()))
            .thenReturn(model);

        String result = geoPointService.buildMap("id", model);

        assertThat(result)
            .isEqualTo("point");
    }

    private static GeoPointData geoPointData() {
        GeoPoint geoPoint = new GeoPoint("id", "title", "description", 1.0, 2.0);
        return new GeoPointData("user", List.of(geoPoint));
    }

    private static GeoProperties.MapProperties mapProperties() {
        return new GeoProperties.MapProperties("title");
    }
}
