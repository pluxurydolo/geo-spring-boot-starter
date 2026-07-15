package com.pluxurydolo.geo.configuration;

import com.pluxurydolo.geo.controller.GeoPointController;
import com.pluxurydolo.geo.dto.GeoPointData;
import com.pluxurydolo.geo.properties.GeoProperties;
import com.pluxurydolo.geo.service.GeoPointService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@ConditionalOnProperty(prefix = "geo.map.point", name = "enabled", havingValue = "true")
public class GeoPointConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public GeoPointController geoPointController(GeoPointService geoPointService) {
        return new GeoPointController(geoPointService);
    }

    @Bean
    @ConditionalOnMissingBean
    public GeoPointService geoPointService(
        Function<String, GeoPointData> geoPointDataProvider,
        GeoProperties geoProperties
    ) {
        return new GeoPointService(geoPointDataProvider, geoProperties);
    }
}
