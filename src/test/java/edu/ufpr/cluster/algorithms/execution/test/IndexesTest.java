/*
 * Copyright 2015, Charter Communications, All rights reserved.
 */
package edu.ufpr.cluster.algorithms.execution.test;

import com.google.common.collect.Lists;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import edu.ufpr.cluster.algorithm.ClusteringAlgorithm;
import edu.ufpr.cluster.algorithm.ClusteringContext;
import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.cluster.algorithms.functions.impl.EucledianDistanceFunction;
import edu.ufpr.cluster.kmeans.KMeansClusteringAlgorithm;
import edu.ufpr.cluster.random.ClusteringRandom;
import edu.ufpr.ge.mapper.impl.ClusteringExpressionGrammarMapper;
import edu.ufpr.jmetal.problem.fitness.DaviesBouldinIndexFitness;
import edu.ufpr.jmetal.problem.fitness.FitnessFunction;
import edu.ufpr.jmetal.problem.fitness.SilhouetteFitness;
import edu.ufpr.jmetal.problem.old.impl.DataInstanceReader;
import edu.ufpr.math.utils.MathUtils;

/**
 *
 *
 * @author Vidal
 */
public class IndexesTest {

    private ClusteringExpressionGrammarMapper mapper;

    private FitnessFunction sillhouete;

    private FitnessFunction davidBouldin;

    @Before
    public void setup() {

        mapper = new ClusteringExpressionGrammarMapper(20);
        mapper.loadGrammar("/clustergrammar.bnf");
        ClusteringRandom.getNewInstance().setSeed(100);
        sillhouete = new SilhouetteFitness();

        davidBouldin = new DaviesBouldinIndexFitness(new EucledianDistanceFunction());

    }

    @Test
    public void testGeneratedAlgorithms() throws FileNotFoundException, IOException {

        // Heart
        // List<Integer> individual =
        // Lists.newArrayList(2, 100, 176, 220, 99, 210, 193, 84, 229, 122, 219,
        // 21, 205, 85, 133, 231, 163, 200, 145,
        // 21, 21, 205, 85, 175, 124, 93, 200, 75, 85, 10, 98, 38, 84, 22, 55,
        // 49, 191, 29, 15, 85, 175, 231, 127,
        // 200, 100, 198, 47, 60, 222, 195, 192, 96, 174, 15, 37, 101, 6, 70, 5,
        // 219, 60, 230, 177, 27, 146, 7);
        //

        // iris
        // List<Integer> individual =
        // Lists.newArrayList(245, 31, 242, 15, 139, 46, 247, 233, 19, 236, 171,
        // 202, 15, 100, 37, 185);

        // diabetes
        // List<Integer> individual = Lists.newArrayList(101, 130, 43, 104, 85,
        // 7, 233, 209, 137, 83, 221, 169, 149, 250,
        // 163, 101, 39, 207, 105, 130, 161, 151, 237, 169, 173, 146, 105, 130,
        // 83, 128, 53, 182, 227, 169, 161, 207,
        // 67, 130, 87, 191, 220, 105, 99, 184, 169, 16, 170, 149, 165, 12, 244,
        // 113, 122, 77, 241, 110, 180, 159, 4,
        // 45, 225, 54, 169, 87, 152, 87, 187, 86, 169, 137, 141, 208, 68, 203,
        // 87, 139, 67, 25, 57, 79, 57, 87, 92,
        // 87, 152, 172, 130, 177, 206, 171, 39, 217, 209, 55, 83, 73, 169, 84,
        // 193, 32, 137, 217, 169, 161, 75, 207,
        // 117, 24, 182, 125, 83, 217, 69, 127, 42, 131, 105, 13, 128, 202, 161,
        // 199, 105, 150, 141, 105, 244, 166,
        // 107, 124, 7, 185, 169, 105, 83, 28, 94, 149, 149, 241, 118, 211, 57,
        // 105, 160, 43, 104, 87, 7, 222, 20, 12,
        // 83, 34, 146, 149, 250, 241, 101, 137, 207, 69, 70, 33, 151, 31, 130,
        // 173, 146, 81, 130, 100, 176, 69, 190,
        // 227, 169, 7, 233, 209, 105, 83, 221, 169, 149, 250, 163, 101, 39,
        // 207, 105, 130, 161, 151, 87, 169, 169,
        // 146, 105, 130, 83, 128, 69, 182, 37, 158, 141, 42, 67, 130, 87, 191,
        // 250, 105, 48, 184, 169, 16, 17, 67, 26,
        // 6, 146, 183, 243, 158, 171, 131, 245, 195, 60, 87, 148, 13, 44, 225,
        // 203, 217, 208, 41, 166, 91, 173, 134,
        // 43, 207, 147, 128, 69, 182, 227, 10, 99, 42, 108, 130, 87, 191, 250,
        // 105, 48, 184, 169, 220, 170, 67, 108,
        // 208, 146, 130, 84, 170, 67, 34, 127, 146, 135, 130, 77, 130, 235, 44,
        // 217, 29, 45, 34, 131, 169, 65, 67,
        // 161, 189, 135, 164, 87, 16, 168, 129, 165, 94, 185, 232, 116, 179,
        // 146, 154, 233, 232, 13, 215, 207, 162,
        // 198, 19, 71);

        // Glass
        List<Integer> individual = Lists.newArrayList(135, 14, 195, 3, 229, 71, 241, 108, 115, 207, 245, 170, 225, 76,
            219, 115, 225, 201, 248, 115, 29, 245, 50, 18, 84, 95, 221, 120, 53, 76, 125, 248, 85, 158, 107);

        List<Point> points = MathUtils.normalizeData(DataInstanceReader.readPoints("/glass.data", "Double", true));

        List<Double> fitnesses = new ArrayList<Double>();

        List<Double> executionTimes = new ArrayList<Double>();

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            System.out.println("Seed: " + i);

            ClusteringRandom.getNewInstance().setSeed(i);

            ClusteringAlgorithm algorithm = mapper.interpret(individual);

            points.stream().forEach(p -> {
                p.clearCluster();
            });
            algorithm.setPoints(points);
            algorithm.setMaxEvaluations(1000);

            long executionTimeStart = System.currentTimeMillis();

            ClusteringContext clusteringContext = algorithm.execute();

            long executionTimeStop = System.currentTimeMillis();

            double executionTime = executionTimeStop - executionTimeStart;

            executionTimes.add(executionTime);

            Double fitness = davidBouldin.apply(clusteringContext);
            System.out.println(fitness + ", k: " + clusteringContext.getClusters().size());

            fitnesses.add(fitness);

            algorithm.clearClusteringContext();

        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Total Execution Time (seconds): " + elapsedTime / 1000);

        List<Double> filteredFitnesses = fitnesses.stream().filter(f -> {
            if (f != Double.MAX_VALUE)
                return true;
            return false;
        }).collect(Collectors.toList());

        DoubleSummaryStatistics summaryStatistics =
            filteredFitnesses.stream().collect(Collectors.summarizingDouble(x -> x));

        System.out.println("Average: " + summaryStatistics.getAverage());
        System.out.println("Min: " + summaryStatistics.getMin());
        System.out.println("Max: " + summaryStatistics.getMax());
        System.out.println("Count: " + summaryStatistics.getCount());
        System.out.println("Std: " + MathUtils.getStdDev(filteredFitnesses, summaryStatistics.getAverage()));

        System.out.println("Time Statistics");

        DoubleSummaryStatistics timeStatisticsSummary =
            executionTimes.stream().collect(Collectors.summarizingDouble(x -> x));

        System.out.println("Average: " + timeStatisticsSummary.getAverage());
        System.out.println("Min: " + timeStatisticsSummary.getMin());
        System.out.println("Max: " + timeStatisticsSummary.getMax());
        System.out.println("Count: " + timeStatisticsSummary.getCount());
        System.out.println("Std: " + MathUtils.getStdDev(executionTimes, timeStatisticsSummary.getAverage()));

        // }
    }

    @Test
    // THIS IS THE WORKING TEST TO TEST THE ALGORITHMS GENERATED BY THE GE
    public void testKmeans() throws FileNotFoundException, IOException {

        List<Point> points = MathUtils.normalizeData(DataInstanceReader.readPoints("/glass.data", "Double", true));

        List<Double> fitnesses = new ArrayList<Double>();

        List<Double> executionTimes = new ArrayList<Double>();

        int k = 2;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            System.out.println("Seed: " + i);

            ClusteringRandom.getNewInstance().setSeed(i);

            points.stream().forEach(p -> {
                p.clearCluster();
            });

            long executionTimeStart = System.currentTimeMillis();
            KMeansClusteringAlgorithm algorithm = new KMeansClusteringAlgorithm(points, new EucledianDistanceFunction(),
                k, points.get(0).getCoordinates().size());

            ClusteringContext clusteringContext = algorithm.execute();

            long executionTimeStop = System.currentTimeMillis();

            double executionTime = executionTimeStop - executionTimeStart;

            executionTimes.add(executionTime);

            Double fitness = davidBouldin.apply(clusteringContext);
            System.out.println(fitness + ", k: " + clusteringContext.getClusters().size());

            fitnesses.add(fitness);

            algorithm.clearClusteringContext();

        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Total Execution Time (seconds): " + elapsedTime / 1000);

        List<Double> filteredFitnesses = fitnesses.stream().filter(f -> {
            if (f != Double.MAX_VALUE)
                return true;
            return false;
        }).collect(Collectors.toList());

        DoubleSummaryStatistics summaryStatistics =
            filteredFitnesses.stream().collect(Collectors.summarizingDouble(x -> x));

        System.out.println("Average: " + summaryStatistics.getAverage());
        System.out.println("Min: " + summaryStatistics.getMin());
        System.out.println("Max: " + summaryStatistics.getMax());
        System.out.println("Count: " + summaryStatistics.getCount());
        System.out.println("Std: " + MathUtils.getStdDev(filteredFitnesses, summaryStatistics.getAverage()));

        System.out.println("Time Statistics");

        DoubleSummaryStatistics timeStatisticsSummary =
            executionTimes.stream().collect(Collectors.summarizingDouble(x -> x));

        System.out.println("Average: " + timeStatisticsSummary.getAverage());
        System.out.println("Min: " + timeStatisticsSummary.getMin());
        System.out.println("Max: " + timeStatisticsSummary.getMax());
        System.out.println("Count: " + timeStatisticsSummary.getCount());
        System.out.println("Std: " + MathUtils.getStdDev(executionTimes, timeStatisticsSummary.getAverage()));

        // }

    }

}
