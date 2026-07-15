package com.pluxurydolo.geo.mapper;

import com.graphhopper.ResponsePath;
import com.graphhopper.util.PointList;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PointsMapperTests {
    private static final PointsMapper MAPPER = new PointsMapper();

    @Test
    void testMapToCoordinates() {
        List<double[]> result = MAPPER.mapToCoordinates(responsePath());

        assertThat(result)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields()
            .isEqualTo(result());
    }

    private static ResponsePath responsePath() {
        PointList points = new PointList();
        points.add(0.0, 2.0);
        points.add(45.0, 22.5);
        points.add(67.5, 90.0);

        ResponsePath responsePath = new ResponsePath();
        responsePath.setPoints(points);

        return responsePath;
    }

    private static List<double[]> result() {
        return List.of(
            new double[]{2.0, 0.0},
            new double[]{22.5, 45.0},
            new double[]{90.0, 67.5}
        );
    }
}
