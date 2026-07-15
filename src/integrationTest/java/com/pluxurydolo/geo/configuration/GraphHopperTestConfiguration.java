package com.pluxurydolo.geo.configuration;

import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.util.PointList;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
public class GraphHopperTestConfiguration {

    @Bean
    public GraphHopper graphHopper() {
        GraphHopper mock = mock(GraphHopper.class);

        when(mock.route(any()))
            .thenReturn(ghResponse());

        return mock;
    }

    private static GHResponse ghResponse() {
        PointList points = new PointList();
        points.add(0.0, 2.0);
        points.add(45.0, 22.5);
        points.add(67.5, 90.0);

        ResponsePath responsePath = new ResponsePath();
        responsePath.setPoints(points);
        responsePath.setDistance(10_000.0);
        responsePath.setTime(600_000L);

        GHResponse ghResponse = new GHResponse();
        ghResponse.add(responsePath);

        return ghResponse;
    }
}
