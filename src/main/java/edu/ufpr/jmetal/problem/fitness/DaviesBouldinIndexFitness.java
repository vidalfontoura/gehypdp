/*
 * Copyright 2015, Charter Communications, All rights reserved.
 */
package edu.ufpr.jmetal.problem.fitness;

import com.google.common.collect.Lists;

import java.util.List;

import edu.ufpr.cluster.algorithm.Cluster;
import edu.ufpr.cluster.algorithm.ClusteringContext;
import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.cluster.algorithms.functions.DistanceFunction;

/**
 *
 *
 * @author Vidal
 */
public class DaviesBouldinIndexFitness implements FitnessFunction {

    private DistanceFunction distanceFunction;

    public DaviesBouldinIndexFitness(DistanceFunction distanceFunction) {
        this.distanceFunction = distanceFunction;
    }

    public Double apply(ClusteringContext clusteringContext) {

        List<Cluster> clusters = clusteringContext.getClusters();
        int n = clusters.size();

        List<Double> maxes = Lists.newArrayList();

        for (int i = 0; i < clusters.size(); i++) {
            Cluster ci = clusters.get(i);

            double max = Double.MIN_VALUE;

            for (int j = 0; j < clusters.size(); j++) {
                Cluster cj = clusters.get(j);
                if (ci == cj) {
                    continue;
                }

                Point centroidI = ci.getCentroid();
                Point centroidJ = cj.getCentroid();
                double d = distanceFunction.apply(Lists.newArrayList(centroidI, centroidJ));

                List<Point> pointsI = ci.getPoints();
                double auxi = 0.0;
                for (Point p : pointsI) {
                    auxi += distanceFunction.apply(Lists.newArrayList(p, centroidI));
                }
                double sigmaI = auxi / pointsI.size();

                List<Point> pointsJ = cj.getPoints();
                double auxj = 0.0;
                for (Point p : pointsJ) {
                    auxj += distanceFunction.apply(Lists.newArrayList(p, centroidJ));
                }
                double sigmaJ = auxj / pointsJ.size();

                double result = sigmaI + sigmaJ / d;

                if (result > max) {
                    max = result;
                }
            }
            maxes.add(max);
        }

        double sum = maxes.stream().mapToDouble(d -> d).sum();
        return sum / n;

    }

}
