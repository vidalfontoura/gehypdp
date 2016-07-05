package edu.ufpr.jmetal.problem;

import com.google.common.collect.Lists;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import edu.ufpr.cluster.algorithm.Cluster;
import edu.ufpr.cluster.algorithm.ClusteringContext;
import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.cluster.algorithms.functions.impl.EucledianDistanceFunction;
import edu.ufpr.jmetal.problem.fitness.FitnessFunction;
import edu.ufpr.jmetal.problem.fitness.SumOfSquaredErrorsFitness;
import edu.ufpr.jmetal.problem.old.impl.DataInstanceReader;

public class RealDataInstanceFitnessFunction {

    private FitnessFunction fitnessFunction;

    @Before
    public void setup() {

        JMetalRandom.getInstance().setSeed(100);
        fitnessFunction = new SumOfSquaredErrorsFitness();
    }

    @Test
    public void test() throws FileNotFoundException, IOException {

        List<Point> pointsALL = DataInstanceReader.readPoints("/points.data", "Double", true);

        List<Point> cluster1 = pointsALL.stream().filter(p -> {

            List<Double> coordinates = p.getCoordinates();

            int last = coordinates.size() - 1;

            Double double1 = coordinates.get(last);

            if (double1 == 0) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        List<Point> cluster2 = pointsALL.stream().filter(p -> {

            List<Double> coordinates = p.getCoordinates();

            int last = coordinates.size() - 1;

            Double double1 = coordinates.get(last);

            if (double1 == 1) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        System.out.println(cluster1.size());
        System.out.println(cluster2.size());

        List<Point> points = pointsALL.stream().map(p -> {
            List<Double> coordinates = p.getCoordinates();
            List<Double> newCoordinates = new ArrayList<Double>();
            for (Double d : coordinates) {
                newCoordinates.add(d);
            }
            int last = newCoordinates.size() - 1;
            newCoordinates.remove(last);
            return new Point(newCoordinates);
        }).collect(Collectors.toList());
        //
        List<Double> clusters = pointsALL.stream().map(p -> {
            List<Double> coordinates = p.getCoordinates();
            int last = coordinates.size() - 1;
            return coordinates.get(last);
        }).collect(Collectors.toList());

        Cluster a = new Cluster();
        Cluster b = new Cluster();

        double c1 = 0;
        double c2 = 1;

        pointsALL.stream().forEach(p -> {
            List<Double> coordinates = p.getCoordinates();
            int last = coordinates.size() - 1;
            Double double1 = coordinates.get(last);
            if (double1 == c1) {
                List<Double> newCoor = new ArrayList<Double>();
                for (int i = 0; i < coordinates.size() - 1; i++) {
                    newCoor.add(coordinates.get(i));
                }
                Point point = new Point(newCoor);
                a.addPoint(point);

            } else if (double1 == c2) {
                List<Double> newCoor = new ArrayList<Double>();
                for (int i = 0; i < coordinates.size() - 1; i++) {
                    newCoor.add(coordinates.get(i));
                }
                Point point = new Point(newCoor);
                b.addPoint(point);
            }

        });

        a.updateCentroid();
        b.updateCentroid();

        a.printCluster();
        b.printCluster();

        EucledianDistanceFunction distanceFunction = new EucledianDistanceFunction();

        List<Point> union = new ArrayList<Point>();
        union.addAll(a.getPoints());
        union.addAll(b.getPoints());

        ClusteringContext clusteringContext = new ClusteringContext(union, Lists.newArrayList(a, b), distanceFunction);

        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);
        //
        // clusters
        //
        //

        // for (Double d: clusters) {
        // System.out.println(d);
        // }
        // System.out.println(clusters.size());

    }

}
