/*
 * Copyright 2015, Charter Communications, All rights reserved.
 */
package edu.ufpr.cluster.algorithms.execution.test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ufpr.cluster.algorithm.Cluster;
import edu.ufpr.cluster.algorithm.ClusteringAlgorithm;
import edu.ufpr.cluster.algorithm.ClusteringContext;
import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.cluster.algorithms.functions.Function;
import edu.ufpr.cluster.algorithms.functions.impl.EucledianDistanceFunction;
import edu.ufpr.cluster.kmeans.KMeansClusteringAlgorithm;
import edu.ufpr.cluster.random.ClusteringRandom;
import edu.ufpr.ge.mapper.impl.ClusteringExpressionGrammarMapper;
import edu.ufpr.jmetal.problem.fitness.FitnessFunction;
import edu.ufpr.jmetal.problem.fitness.SilhouetteFitness;
import edu.ufpr.jmetal.problem.old.impl.DataInstanceReader;
import edu.ufpr.math.utils.MathUtils;

/**
 *
 *
 * @author Vidal
 */
public class ExecutionTestClusteringAlgorithms {

    private ClusteringExpressionGrammarMapper mapper;

    private FitnessFunction fitnessFunction;

    @Before
    public void setup() {

        mapper = new ClusteringExpressionGrammarMapper(20);
        mapper.loadGrammar("/clustergrammar.bnf");
        ClusteringRandom.getNewInstance().setSeed(100);
        fitnessFunction = new SilhouetteFitness();
        // fitnessFunction = new SimpleClusteringFitness();

    }

    /**
     * <pre>
     *  24,120,154,179,66,23,18,32,99,43,101,229,225,146,217,124,245,195,238
     * 
     * 
     * </pre>
     * 
     * @throws IOException
     * @throws FileNotFoundException
     * 
     *
     */
    @Test
    public void testAlgorithmGenerated1() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance =
            Lists.newArrayList(24, 120, 154, 179, 66, 23, 18, 32, 99, 43, 101, 229, 225, 146, 217, 124, 245, 195, 238);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("RandomCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("ChebyshevDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(9, algorithm.getInitialK());
        Assert.assertTrue(algorithm.getFunctions().size() == 1);

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);

        Assert.assertEquals("MoveBetweenClustersFunction", function0.toString());

        List<Point> points = DataInstanceReader.readPoints("/points.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();
        List<Point> points2 = clusteringContext.getPoints();
        for (Point p : points2) {
            System.out.println(p);
            System.out.println(p.getCluster());
            System.out.println();
        }

        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    @Test
    public void testAlgorithmGenerated2() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance = Lists.newArrayList(14, 243, 33, 38, 243, 33, 38, 84);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("ChebyshevDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(10, algorithm.getInitialK());
        Assert.assertEquals(2, algorithm.getFunctions().size());

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);

        Assert.assertEquals("MoveBetweenClustersFunction", function0.toString());
        Assert.assertEquals("MoveNearPointFunction", function1.toString());

        List<Point> points = DataInstanceReader.readPoints("/points.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        List<Cluster> clusters = clusteringContext.getClusters();
        for (Cluster cluster : clusters) {
            System.out.println(cluster.getCentroid());
            System.out.println(cluster.getPoints().size());
            System.out.println();
        }
        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    @Test
    public void testAlgorithmGenerated3() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance = Lists.newArrayList(1, 125, 239, 42, 167, 37, 39, 224, 214, 61, 151, 9, 213, 85,
            193, 87, 135, 170, 8, 94, 189);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("EucledianDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(9, algorithm.getInitialK());
        Assert.assertEquals(3, algorithm.getFunctions().size());

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);

        Assert.assertEquals("MoveAveragePointFunction", function0.toString());
        Assert.assertEquals("MoveNearPointFunction", function1.toString());

        List<Point> points = DataInstanceReader.readPoints("/points.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        List<Cluster> clusters = clusteringContext.getClusters();
        for (Cluster cluster : clusters) {
            System.out.println(cluster.getCentroid());
            System.out.println(cluster.getPoints().size());
            System.out.println();
        }
        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    @Test
    public void testAlgorithmGenerated4() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance = Lists.newArrayList(14, 243, 33, 38, 21);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("ChebyshevDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(10, algorithm.getInitialK());
        Assert.assertEquals(3, algorithm.getFunctions().size());

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);
        Function<ClusteringContext> function2 = algorithm.getFunctions().get(2);

        Assert.assertEquals("MoveNearPointFunction", function0.toString());
        Assert.assertEquals("MoveBetweenClustersFunction", function1.toString());
        Assert.assertEquals("SplitClustersFunction", function2.toString());

        List<Point> points = DataInstanceReader.readPoints("/points.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        List<Cluster> clusters = clusteringContext.getClusters();
        for (Cluster cluster : clusters) {
            System.out.println(cluster.getCentroid());
            System.out.println(cluster.getPoints().size());
            System.out.println();
        }
        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    @Test
    public void testAlgorithmGenerated5() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance = Lists.newArrayList(139, 120, 194, 181, 201, 209, 167, 3, 67, 236, 206, 142, 120,
            159, 179, 66, 23, 18, 32, 99, 43, 101, 229, 225, 146, 217, 124, 107, 195, 33);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("RandomCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("ManhattanDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(4, algorithm.getInitialK());
        Assert.assertEquals(4, algorithm.getFunctions().size());

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);
        Function<ClusteringContext> function2 = algorithm.getFunctions().get(2);
        Function<ClusteringContext> function3 = algorithm.getFunctions().get(3);

        Assert.assertEquals("MoveNearPointFunction", function0.toString());
        Assert.assertEquals("MoveBetweenClustersFunction", function1.toString());
        Assert.assertEquals("SplitClustersFunction", function2.toString());
        Assert.assertEquals("MoveAveragePointFunction", function3.toString());

        List<Point> points = DataInstanceReader.readPoints("/points.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        List<Cluster> clusters = clusteringContext.getClusters();
        for (Cluster cluster : clusters) {
            System.out.println(cluster.getCentroid());
            System.out.println(cluster.getPoints().size());
            System.out.println();
        }
        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    @Test
    public void testAlgorithmGenerated6Points() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance =
            Lists.newArrayList(108, 13, 119, 67, 85, 225, 205, 179, 61, 97, 9, 52, 85, 198, 87, 246, 189);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("ManhattanDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(4, algorithm.getInitialK());
        Assert.assertEquals(13, algorithm.getFunctions().size());

        for (Function f : algorithm.getFunctions()) {
            System.out.println(f.toString());
        }
        System.out.println();

        List<Point> points = DataInstanceReader.readPoints("/points.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        for (Point p : clusteringContext.getPoints()) {
            System.out.println(p + " " + p.getCluster());
        }

        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    @Test
    public void testAlgorithmGenerated6MorePoints() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance =
            Lists.newArrayList(108, 13, 119, 67, 85, 225, 205, 179, 61, 97, 9, 52, 85, 198, 87, 246, 189);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("ManhattanDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(4, algorithm.getInitialK());
        Assert.assertEquals(13, algorithm.getFunctions().size());

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);
        Function<ClusteringContext> function2 = algorithm.getFunctions().get(2);
        Function<ClusteringContext> function3 = algorithm.getFunctions().get(3);

        for (Function f : algorithm.getFunctions()) {
            System.out.println(f.toString());
        }
        System.out.println();

        List<Point> points = DataInstanceReader.readPoints("/points.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        List<Cluster> clusters = clusteringContext.getClusters();
        for (Cluster cluster : clusters) {
            System.out.println("Centroid: " + cluster.getCentroid());
            System.out.println("Size: " + cluster.getPoints().size());
            System.out.println("Points: " + cluster.getPoints().toString());
            System.out.println();
        }
        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    @Test
    public void testAlgorithmGenerated6_20Points() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance =
            Lists.newArrayList(108, 13, 119, 67, 85, 225, 205, 179, 61, 97, 9, 52, 85, 198, 87, 246, 189);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("ManhattanDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(4, algorithm.getInitialK());
        Assert.assertEquals(13, algorithm.getFunctions().size());

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);
        Function<ClusteringContext> function2 = algorithm.getFunctions().get(2);
        Function<ClusteringContext> function3 = algorithm.getFunctions().get(3);

        for (Function f : algorithm.getFunctions()) {
            System.out.println(f.toString());
        }
        System.out.println();

        List<Point> points = DataInstanceReader.readPoints("/20Points.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        List<Cluster> clusters = clusteringContext.getClusters();
        for (Cluster cluster : clusters) {
            System.out.println("Centroid: " + cluster.getCentroid());
            System.out.println("Size: " + cluster.getPoints().size());
            System.out.println("Points: " + cluster.getPoints().toString());
            System.out.println();
        }
        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    @Test
    public void testAlgorithmGenerated6_50Points() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance =
            Lists.newArrayList(108, 13, 119, 67, 85, 225, 205, 179, 61, 97, 9, 52, 85, 198, 87, 246, 189);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("ManhattanDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(4, algorithm.getInitialK());
        Assert.assertEquals(13, algorithm.getFunctions().size());

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);
        Function<ClusteringContext> function2 = algorithm.getFunctions().get(2);
        Function<ClusteringContext> function3 = algorithm.getFunctions().get(3);

        for (Function f : algorithm.getFunctions()) {
            System.out.println(f.toString());
        }
        System.out.println();

        List<Point> points = DataInstanceReader.readPoints("/50-random-points.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        List<Cluster> clusters = clusteringContext.getClusters();
        for (Cluster cluster : clusters) {
            System.out.println("Centroid: " + cluster.getCentroid());
            System.out.println("Size: " + cluster.getPoints().size());
            System.out.println("Points: " + cluster.getPoints().toString());
            System.out.println();
        }
        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    @Test
    public void testAlgorithmGenerated6Diabetes() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance =
            Lists.newArrayList(108, 13, 119, 67, 85, 225, 205, 179, 61, 97, 9, 52, 85, 198, 87, 246, 189);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("ManhattanDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(4, algorithm.getInitialK());
        Assert.assertEquals(13, algorithm.getFunctions().size());

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);
        Function<ClusteringContext> function2 = algorithm.getFunctions().get(2);
        Function<ClusteringContext> function3 = algorithm.getFunctions().get(3);

        for (Function f : algorithm.getFunctions()) {
            System.out.println(f.toString());
        }
        System.out.println();

        List<Point> points = DataInstanceReader.readPoints("/prima-indians-diabetes.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        List<Cluster> clusters = clusteringContext.getClusters();
        for (Cluster cluster : clusters) {
            System.out.println("Centroid: " + cluster.getCentroid());
            System.out.println("Size: " + cluster.getPoints().size());
            System.out.println("Points: " + cluster.getPoints().toString());
            System.out.println();
        }

        List<Point> pointsClustered = clusteringContext.getPoints();
        List<Point> pointsWithoutCluster = pointsClustered.stream().filter(p -> {
            return p.getCluster() == null;
        }).collect(Collectors.toList());

        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    @Test
    public void testAlgorithmGenerated7Points() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance = Lists.newArrayList(205, 112, 86, 3, 157, 4, 71, 133);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("RandomCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("EucledianDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(6, algorithm.getInitialK());
        Assert.assertEquals(4, algorithm.getFunctions().size());

        for (Function f : algorithm.getFunctions()) {
            System.out.println(f.toString());
        }
        System.out.println();

        List<Point> points = DataInstanceReader.readPoints("/points.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        // List<Cluster> clusters = clusteringContext.getClusters();
        // for (Cluster cluster : clusters) {
        // System.out.println("Centroid: " + cluster.getCentroid());
        // System.out.println("Size: " + cluster.getPoints().size());
        // System.out.println("Points: " + cluster.getPoints().toString());
        // System.out.println();
        // }

        for (Point p : clusteringContext.getPoints()) {
            System.out.println(p + " " + p.getCluster());
        }

        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    @Test
    public void testAlgorithmGenerated7_20Points() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance = Lists.newArrayList(205, 112, 86, 3, 157, 4, 71, 133);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("RandomCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("EucledianDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(6, algorithm.getInitialK());
        Assert.assertEquals(4, algorithm.getFunctions().size());

        for (Function f : algorithm.getFunctions()) {
            System.out.println(f.toString());
        }
        System.out.println();

        List<Point> points = DataInstanceReader.readPoints("/20points.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        // List<Cluster> clusters = clusteringContext.getClusters();
        // for (Cluster cluster : clusters) {
        // System.out.println("Centroid: " + cluster.getCentroid());
        // System.out.println("Size: " + cluster.getPoints().size());
        // System.out.println("Points: " + cluster.getPoints().toString());
        // System.out.println();
        // }

        for (Point p : clusteringContext.getPoints()) {
            System.out.println(p + " " + p.getCluster());
        }

        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    @Test
    public void testAlgorithmGenerated7MorePoints() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance = Lists.newArrayList(205, 112, 86, 3, 157, 4, 71, 133);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("RandomCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("EucledianDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(6, algorithm.getInitialK());
        Assert.assertEquals(4, algorithm.getFunctions().size());

        for (Function f : algorithm.getFunctions()) {
            System.out.println(f.toString());
        }
        System.out.println();

        List<Point> points = DataInstanceReader.readPoints("/points.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        // List<Cluster> clusters = clusteringContext.getClusters();
        // for (Cluster cluster : clusters) {
        // System.out.println("Centroid: " + cluster.getCentroid());
        // System.out.println("Size: " + cluster.getPoints().size());
        // System.out.println("Points: " + cluster.getPoints().toString());
        // System.out.println();
        // }

        for (Point p : clusteringContext.getPoints()) {
            System.out.println(p + " " + p.getCluster());
        }

        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    @Test
    public void testAlgorithmGenerated7_50Points() throws FileNotFoundException, IOException

    {

        List<Integer> grammarInstance = Lists.newArrayList(205, 112, 86, 3, 157, 4, 71, 133);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("RandomCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("EucledianDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(6, algorithm.getInitialK());
        Assert.assertEquals(4, algorithm.getFunctions().size());

        for (Function f : algorithm.getFunctions()) {
            System.out.println(f.toString());
        }
        System.out.println();

        List<Point> points = DataInstanceReader.readPoints("/50-random-points.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        // List<Cluster> clusters = clusteringContext.getClusters();
        // for (Cluster cluster : clusters) {
        // System.out.println("Centroid: " + cluster.getCentroid());
        // System.out.println("Size: " + cluster.getPoints().size());
        // System.out.println("Points: " + cluster.getPoints().toString());
        // System.out.println();
        // }

        for (Point p : clusteringContext.getPoints()) {
            System.out.println(p + " " + p.getCluster());
        }

        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    @Test
    public void testAlgorithmGenerated7Diabetes() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance = Lists.newArrayList(205, 112, 86, 3, 157, 4, 71, 133);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("RandomCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("EucledianDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(6, algorithm.getInitialK());
        Assert.assertEquals(4, algorithm.getFunctions().size());

        for (Function f : algorithm.getFunctions()) {
            System.out.println(f.toString());
        }
        System.out.println();

        List<Point> points = DataInstanceReader.readPoints("/prima-indians-diabetes.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        // for (Point p : clusteringContext.getPoints()) {
        // System.out.println(p + " " + p.getCluster());
        // }

        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);
    }

    @Test
    public void testAlgorithmGenerated8Points() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance = Lists.newArrayList(199);
        // List<Integer> grammarInstance = Lists.newArrayList(67, 55, 66, 50,
        // 175, 111, 215, 137);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);

        for (Function f : algorithm.getFunctions()) {
            System.out.println(f.toString());
        }
        System.out.println();

        List<Point> points = MathUtils.normalizeData(DataInstanceReader.readPoints("/points.data", "Double", true));
        // List<Point> points =
        // MathUtils.normalizeData(DataInstanceReader.readPoints("/points.data"));
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        for (Point p : clusteringContext.getPoints()) {
            System.out.println(p + " " + p.getCluster());
        }

        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    @Test
    public void testAlgorithmGenerated9Diabetes() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance = Lists.newArrayList(91, 157, 191, 125, 85, 125, 134);
        // List<Integer> grammarInstance = Lists.newArrayList(67, 55, 66, 50,
        // 175, 111, 215, 137);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);

        for (Function f : algorithm.getFunctions()) {
            System.out.println(f.toString());
        }
        System.out.println();

        List<Point> points =
            MathUtils.normalizeData(DataInstanceReader.readPoints("/prima-indians-diabetes.data", "Double", true));
        // List<Point> points =
        // MathUtils.normalizeData(DataInstanceReader.readPoints("/points.data"));
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        for (Point p : clusteringContext.getPoints()) {
            System.out.println(p + " " + p.getCluster());
        }

        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);
    }

    @Test
    public void testAlgorithmGenerated10Diabetes() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance = Lists.newArrayList(187, 83, 102, 50, 243, 196, 135, 219);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);

        for (Function f : algorithm.getFunctions()) {
            System.out.println(f.toString());
        }
        System.out.println();

        List<Point> points =
            MathUtils.normalizeData(DataInstanceReader.readPoints("/prima-indians-diabetes.data", "Double", true));
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);
    }

    @Test
    public void testAlgorithmGeneratedHeart2() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance = Lists.newArrayList(35, 106, 86, 76, 63, 100, 121, 4, 57, 145, 165, 101, 183,
            119, 149, 228, 216, 20, 10, 246, 198, 125, 216, 20, 91, 24, 52, 127, 93, 196, 5, 210, 121, 165, 57, 24, 192,
            101, 183, 119, 149, 213, 216, 20, 232, 32, 38, 139, 4, 125, 142, 4, 216, 165, 101, 237);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);

        List<Point> points = MathUtils.normalizeData(DataInstanceReader.readPoints("/heart.data", "Double", true));
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }
    //

    @Test
    public void testKmeansNormalizeDenormalizeData() throws FileNotFoundException, IOException {

        List<Point> points = DataInstanceReader.readPoints("/points.data", "Double", true);

        KMeansClusteringAlgorithm algorithm =
            new KMeansClusteringAlgorithm(points, new EucledianDistanceFunction(), 2, 2);

        ClusteringContext clusteringContext = algorithm.execute();

        clusteringContext.getClusters().stream().forEach(c -> c.printCluster());

        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    // 203,56,82,97,107,54,47,80,157,6,73,95,79,91,145,80,157,6,73,95,105,50,207,241,162,85,49,183,162,85,204,96,204,54

    @Test
    public void testAlgorithmGenerated1Iris() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance = Lists.newArrayList(203, 56, 82, 97, 107, 54, 47, 80, 157, 6, 73, 95, 79, 91,
            145, 80, 157, 6, 73, 95, 105, 50, 207, 241, 162, 85, 49, 183, 162, 85, 204, 96, 204, 54);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);

        List<Point> points = MathUtils.normalizeData(DataInstanceReader.readPoints("/iris.data", "Double", true));
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
        System.setOut(out);

        points = MathUtils.denormalizeData(clusteringContext.getPoints(), 7.9, 0.1);
        for (Point p : clusteringContext.getPoints()) {
            System.out.println(p + " " + p.getCluster());
        }

        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);
    }

    @Test
    public void testDiabetesTrainGlassValidation10Seeds() throws FileNotFoundException, IOException {

        ClusteringRandom.getNewInstance().setSeed(100);
        List<Point> points = MathUtils.normalizeData(DataInstanceReader.readPoints("/glass.data", "Double", true));

        List<Integer> grammarInstance = Lists.newArrayList(91, 157, 191, 125, 85, 125, 133);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);

        List<Double> fitnesses = new ArrayList<Double>();

        ClusteringRandom.getNewInstance().setSeed(100);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            System.out.println("Seed: " + i);

            algorithm.setPoints(points);
            ClusteringContext clusteringContext = algorithm.execute();

            Double fitness = fitnessFunction.apply(clusteringContext);

            System.out.println(fitness);

            algorithm.clearClusteringContext();

            fitnesses.add(fitness);

        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Time (seconds): " + elapsedTime / 1000);

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

    }

    @Test
    // THIS IS THE WORKING TEST TO TEST THE ALGORITHMS GENERATED BY THE GE
    public void testAlgorithmGeneratedIris() throws FileNotFoundException, IOException {

        List<Integer> individual = Lists.newArrayList(63, 49, 22, 104, 165, 243, 247, 221, 141, 182, 177, 16, 247, 235,
            153, 92, 67, 117, 189, 156, 153, 40, 165, 16, 225, 235, 153, 247, 67, 117, 141, 182, 153, 131, 165, 122,
            141, 186, 153, 164, 89, 70, 125, 235, 107, 25, 49, 182, 49, 98, 65, 165, 170, 170, 247, 45, 58, 165, 27,
            175, 149, 94, 61, 141, 49, 243, 234, 235, 219, 247, 108, 49, 112, 213, 101, 53, 76, 40, 189, 49, 248, 132,
            199, 247, 141, 182, 95, 16, 66, 235, 49, 156, 114, 114, 231, 72, 107, 28, 144, 165, 235, 49, 185, 4, 12, 76,
            92, 165, 83, 108, 40, 235, 135, 38, 94, 171, 31, 61, 126, 203, 61, 165, 77, 126, 144, 211, 178, 247, 112,
            73, 147, 97, 194, 77, 247, 165, 55, 28, 151, 77, 172, 15, 241, 236, 247, 235, 83, 247, 171, 227, 83, 138,
            172, 176, 165, 49, 95, 49, 191, 117, 9, 97, 49, 211, 235, 231, 156, 240, 15, 247, 72, 84, 28, 96, 165, 235,
            49, 199, 19, 235, 76, 49, 165, 42, 108, 225, 10, 147, 82, 65, 80, 165, 49, 219, 165, 49, 49, 10, 165, 187,
            98);

        List<Point> points =
            MathUtils.normalizeData(DataInstanceReader.readPoints("/prima-indians-diabetes.data", "Double", true));

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
            algorithm.setMaxEvaluations(500);

            long executionTimeStart = System.currentTimeMillis();

            ClusteringContext clusteringContext = algorithm.execute();

            long executionTimeStop = System.currentTimeMillis();

            double executionTime = executionTimeStop - executionTimeStart;

            executionTimes.add(executionTime);

            Double fitness = fitnessFunction.apply(clusteringContext);
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

        List<Point> points =
            MathUtils.normalizeData(DataInstanceReader.readPoints("/prima-indians-diabetes.data", "Double", true));

        List<Double> fitnesses = new ArrayList<Double>();

        List<Double> executionTimes = new ArrayList<Double>();

        int k = 2;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
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

            Double fitness = fitnessFunction.apply(clusteringContext);
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

    public static Set<List<Integer>> loadFile(String file) throws IOException {

        int count = 0;
        Set<List<Integer>> setList = Sets.newHashSet();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;

            // Read File Line By Line
            List<Integer> values = null;
            while ((line = br.readLine()) != null) {
                if (count > 9) {
                    break;
                }

                // Print the content on the console
                values = Lists.newArrayList();
                String[] vet = line.trim().split(" ");
                for (String value : vet) {
                    values.add(Integer.valueOf(value));
                }

                setList.add(values);
                count++;
            }

        }
        return setList;

    }

    @Test
    public void test() {

        BigDecimal ponto = new BigDecimal(4.1);
        BigDecimal max = new BigDecimal(7.9);
        BigDecimal min = new BigDecimal(0.1);

        BigDecimal diffMaxMin = max.subtract(min);
        BigDecimal aux = ponto.subtract(min);
        aux = aux.divide(diffMaxMin, 20, RoundingMode.CEILING);

        System.out.println(aux);

        BigDecimal aux2 = aux.multiply(diffMaxMin).add(min);

        System.out.println(aux2);

    }

}
