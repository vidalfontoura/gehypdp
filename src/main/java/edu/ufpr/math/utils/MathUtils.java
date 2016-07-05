/*
 * Copyright 2015, Charter Communications, All rights reserved.
 */
package edu.ufpr.math.utils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import edu.ufpr.cluster.algorithm.Point;

/**
 *
 *
 * @author Vidal
 */
public class MathUtils {

    public static double getVariance(List<Double> data, double avg) {

        double mean = avg;
        double temp = 0;
        for (double a : data)
            temp += (mean - a) * (mean - a);
        return temp / data.size();
    }

    public static double getStdDev(List<Double> data, double avg) {

        return Math.sqrt(getVariance(data, avg));
    }

    public static List<Point> normalizeData(List<Point> points) {

        Double max = Double.MIN_VALUE;
        Double min = Double.MAX_VALUE;
        for (Point point : points) {
            Optional<Double> maxPoint = point.getCoordinates().stream().max((o1, o2) -> o1.compareTo(o2));
            Optional<Double> minPoint = point.getCoordinates().stream().max((o1, o2) -> o2.compareTo(o1));
            if (maxPoint.get() > max) {
                max = maxPoint.get();
            }
            if (minPoint.get() < min) {
                min = minPoint.get();
            }
        }

        final double maxf = max;
        final double minf = min;

        return points.stream().map(p -> {

            List<Double> coordinates =
                p.getCoordinates().stream().map(c -> c = (c - minf) / (maxf - minf)).collect(Collectors.toList());
            p.setCoordinates(coordinates);
            return p;
        }).collect(Collectors.toList());
    }

    public static List<Point> denormalizeData(List<Point> points, double max, double min) {

        final double diffMaxMin = max - min;
        final double minf = min;

        return points.stream().map(p -> {

            List<Double> coordinates =
                p.getCoordinates().stream().map(c -> c = (c * diffMaxMin) + minf).collect(Collectors.toList());
            p.setCoordinates(coordinates);
            return p;
        }).collect(Collectors.toList());

    }

}
