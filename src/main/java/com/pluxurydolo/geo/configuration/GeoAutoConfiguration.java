package com.pluxurydolo.geo.configuration;

import com.pluxurydolo.geo.properties.GeoProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({
    GraphHopperConfiguration.class,
    GeoPointConfiguration.class,
    GeoRouteConfiguration.class
})
@EnableConfigurationProperties(GeoProperties.class)
public class GeoAutoConfiguration {
}
