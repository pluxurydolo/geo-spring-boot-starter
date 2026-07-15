package com.pluxurydolo.geo.configuration;

import com.graphhopper.GraphHopper;
import com.pluxurydolo.geo.controller.GeoRouteController;
import com.pluxurydolo.geo.dto.GeoRouteData;
import com.pluxurydolo.geo.mapper.PointsMapper;
import com.pluxurydolo.geo.properties.GeoProperties;
import com.pluxurydolo.geo.service.GeoRouteService;
import com.pluxurydolo.geo.service.RoutingService;
import com.pluxurydolo.geo.util.YandexMapsUrlBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@ConditionalOnProperty(prefix = "geo.map.route", name = "enabled", havingValue = "true")
public class GeoRouteConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public GeoRouteController geoRouteController(GeoRouteService geoRouteService) {
        return new GeoRouteController(geoRouteService);
    }

    @Bean
    @ConditionalOnMissingBean
    public GeoRouteService geoRouteService(
        Function<String, GeoRouteData> geoRouteDataProvider,
        RoutingService routingService,
        PointsMapper pointsMapper,
        YandexMapsUrlBuilder yandexMapsUrlBuilder,
        GeoProperties geoProperties
    ) {
        return new GeoRouteService(geoRouteDataProvider, routingService, pointsMapper, yandexMapsUrlBuilder, geoProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public RoutingService routingService(GraphHopper graphHopper) {
        return new RoutingService(graphHopper);
    }

    @Bean
    @ConditionalOnMissingBean
    public PointsMapper pointsMapper() {
        return new PointsMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public YandexMapsUrlBuilder yandexUrlBuilder() {
        return new YandexMapsUrlBuilder();
    }
}
