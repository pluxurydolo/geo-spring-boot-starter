package com.pluxurydolo.geo.mapper;

import com.graphhopper.ResponsePath;
import com.graphhopper.util.PointList;

import java.util.List;
import java.util.stream.IntStream;

public class PointsMapper {
    public List<double[]> mapToCoordinates(ResponsePath responsePath) {
        PointList points = responsePath.getPoints();
        int size = points.size();

        return IntStream.range(0, size)
            .mapToObj(point -> new double[]{points.getLon(point), points.getLat(point)})
            .toList();
    }
}
