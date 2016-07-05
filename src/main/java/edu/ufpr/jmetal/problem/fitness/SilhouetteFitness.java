package edu.ufpr.jmetal.problem.fitness;

import com.google.common.collect.Lists;

import java.util.List;

import edu.ufpr.cluster.algorithm.Cluster;
import edu.ufpr.cluster.algorithm.ClusteringContext;
import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.cluster.algorithms.functions.DistanceFunction;
import edu.ufpr.cluster.algorithms.functions.impl.EucledianDistanceFunction;

public class SilhouetteFitness implements FitnessFunction {

    DistanceFunction distanceFunction = null;

    public Double apply(ClusteringContext clusteringContext) {

        // If no cluster is empty && and if each point belongs to only one
        // cluster
        List<Cluster> clusters = clusteringContext.getClusters();
        // clusters.stream().forEach(p -> p.printCluster());
        List<Point> allPoints = clusteringContext.getPoints();

        distanceFunction = new EucledianDistanceFunction();

        // If exists more than 10 cluster it will be penalized hard
        if (clusters.size() > 15) {
            // Mudar intervalo no initial k
            System.out.println("Nao deveria acontecer");
            return -1.0;
        }

        // If exists any cluster if no points it will penalize hard the solution
        // for (Cluster c : clusters) {
        // List<Point> points = c.getPoints();
        // if (points == null || points.size() == 0) {
        // return -1.0;
        // }
        // }

        // If exists one point that is not clustered it will penalize hard the
        // solution
        // for (Point p : allPoints) {
        // if (p.getCluster() == null) {
        // return -1.0;
        // }
        // }

        // If all points were assigned for just one cluster
        // if (clusters.size() == 1) {
        // return -1.0;
        // }

        // Calculate fitness
        double fitness = calculateFitness(clusters, allPoints);
        return fitness;

    }

    public double avgD(Point point, List<Point> points) {

        double sum = 0.0;

        for (Point p : points) {
            sum += distanceFunction.apply(Lists.newArrayList(point, p));
        }

        return sum / points.size();
    }

    public double minD(Point point, List<Cluster> clusters) {

        double min = Double.MAX_VALUE;

        for (Cluster c : clusters) {
            if (!point.getCluster().equals(c)) {
                double sum = 0.0;
                for (Point p : c.getPoints()) {
                    sum += distanceFunction.apply(Lists.newArrayList(point, p));
                }
                sum = sum / c.getPoints().size();
                min = (sum < min) ? sum : min;
            }
        }

        return (min == Double.MAX_VALUE) ? 0 : min;
    }

    public double calculateFitness(List<Cluster> clusters, List<Point> allPoints) {

        double f = 0.0;

        for (Cluster c : clusters) {
            if (c.getPoints().size() == 1) {
                f += 0.0;
            } else {
                for (Point p : c.getPoints()) {
                    double a = avgD(p, c.getPoints());
                    double b = minD(p, clusters);
                    f += (a > b) ? (b - a) / a : (b - a) / b;
                    // System.out.println(p+" "+p.getCluster()+"\n"+a+"\n"+b);
                }
            }
        }

        f = f / allPoints.size();

        return f;
    }

}
