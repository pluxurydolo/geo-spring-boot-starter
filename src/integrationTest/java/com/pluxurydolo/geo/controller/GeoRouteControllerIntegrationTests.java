package com.pluxurydolo.geo.controller;

import com.pluxurydolo.geo.base.AbstractControllerIntegrationTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GeoRouteControllerIntegrationTests extends AbstractControllerIntegrationTests {

    @Test
    void testBuildMap() {
        webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/geo/route")
                .queryParam("id", "user")
                .build())
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(result -> {
                byte[] responseBody = result.getResponseBody();

                assertThat(responseBody)
                    .hasSize(1969);
            });
    }

    @Test
    void testBuildRoute() {
        webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/geo/route/waypoints")
                .queryParam("id", "user")
                .queryParam("profileName", "foot")
                .build())
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.profile").isEqualTo("foot")
            .jsonPath("$.distance").isEqualTo(10000.0)
            .jsonPath("$.distanceKm").isEqualTo(10.0)
            .jsonPath("$.time").isEqualTo(600000)
            .jsonPath("$.timeMinutes").isEqualTo(10)
            .jsonPath("$.points.length()").isEqualTo(3)
            .jsonPath("$.points[0][0]").isEqualTo(2.0)
            .jsonPath("$.points[0][1]").isEqualTo(0.0)
            .jsonPath("$.points[1][0]").isEqualTo(22.5)
            .jsonPath("$.points[1][1]").isEqualTo(45.0)
            .jsonPath("$.points[2][0]").isEqualTo(90.0)
            .jsonPath("$.points[2][1]").isEqualTo(67.5)
            .jsonPath("$.pointCount").isEqualTo(5)
            .jsonPath("$.routePointCount").isEqualTo(3);
    }
}
