package edu.ufpr.jmetal.problem.old.impl;

import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.ufpr.cluster.algorithm.Point;

public class DataInstanceReader {

    // TODO: PLEASE REFACTOR THIS METHOD ASAP
    public static List<Point> readPoints(String fileName, String dataType, boolean classIncluded)
        throws FileNotFoundException, IOException {

        Set<Point> points = new HashSet<Point>();
        try (BufferedReader br =
            new BufferedReader(new InputStreamReader(DataInstanceReader.class.getResourceAsStream(fileName)))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Point point = new Point();
                // TODO: This will need to change since the databases files are
                // not in the same format
                for (int i = 0; i < values.length; i++) {

                    if (classIncluded) {
                        if (i != values.length - 1) {
                            if (dataType.equals("Double")) {
                                point.getCoordinates().add(Double.valueOf(values[i]));
                            } else if (dataType.equals("Integer")) {
                                point.getCoordinates().add((double) Integer.valueOf(values[i]));
                            }

                        }
                    } else {
                        if (dataType.equals("Double")) {
                            point.getCoordinates().add(Double.valueOf(values[i]));
                        } else if (dataType.equals("Integer")) {
                            point.getCoordinates().add((double) Integer.valueOf(values[i]));
                        }

                    }

                }
                points.add(point);
            }
        }
        return Lists.newArrayList(points);
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {

        List<Point> readPoints = DataInstanceReader.readPoints("/prima-indians-diabetes.data", "Double", true);
        for (Point point : readPoints) {

            List<Double> coordinates = point.getCoordinates();
            System.out.println(coordinates.toString());
            System.out.println(point.getCluster());
        }
    }
}
