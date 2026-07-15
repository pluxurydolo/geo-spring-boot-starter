package com.pluxurydolo.geo.service;

import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.pluxurydolo.geo.dto.RoutePoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.pluxurydolo.geo.dto.Profile.FOOT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoutingServiceTests {

    @Mock
    private GraphHopper graphHopper;

    @Mock
    private GHResponse ghResponse;

    @InjectMocks
    private RoutingService routingService;

    @Test
    void testGetRouteWithWaypoints() {
        when(graphHopper.route(any()))
            .thenReturn(ghResponse);
        when(ghResponse.getBest())
            .thenReturn(responsePath());

        ResponsePath result = routingService.getRouteWithWaypoints(routePoints(), FOOT);

        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(responsePath());
    }

    private static List<RoutePoint> routePoints() {
        return List.of(
            new RoutePoint(0.0, 2.0, "label1"),
            new RoutePoint(45.0, 22.5, "label2"),
            new RoutePoint(67.5, 90.0, "label3")
        );
    }

    private static ResponsePath responsePath() {
        return new ResponsePath();
    }
}
