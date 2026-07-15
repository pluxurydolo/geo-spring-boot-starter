package com.pluxurydolo.geo.controller;

import com.pluxurydolo.geo.base.AbstractControllerIntegrationTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GeoPointControllerIntegrationTests extends AbstractControllerIntegrationTests {

    @Test
    void testBuildMap() {
        webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/geo/point")
                .queryParam("id", "user")
                .build())
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(result -> {
                byte[] responseBody = result.getResponseBody();

                assertThat(responseBody)
                    .hasSize(1645);
            });
    }
}
