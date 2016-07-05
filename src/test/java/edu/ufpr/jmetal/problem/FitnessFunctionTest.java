/*
 */
package edu.ufpr.jmetal.problem;

import com.google.common.collect.Lists;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import edu.ufpr.cluster.random.ClusteringRandom;
import edu.ufpr.ge.mapper.impl.ClusteringExpressionGrammarMapper;
import edu.ufpr.jmetal.problem.fitness.FitnessFunction;
import edu.ufpr.jmetal.problem.fitness.SumOfSquaredErrorsFitness;
import edu.ufpr.jmetal.problem.old.impl.DataInstanceReader;
import edu.ufpr.math.utils.MathUtils;

/**
 *
 *
 * @author Vidal
 */
public class FitnessFunctionTest {

    private ClusteringExpressionGrammarMapper mapper;

    private FitnessFunction fitnessFunction;

    @Before
    public void setup() {

        mapper = new ClusteringExpressionGrammarMapper(20);
        mapper.loadGrammar("/clustergrammar.bnf");
        ClusteringRandom.getNewInstance().setSeed(100);
        fitnessFunction = new SumOfSquaredErrorsFitness();
    }

    /**
     * <pre>
     *	85,204,128,162,222,31,207,1,52,
     *  Penalized Fitness Double.MaxValue
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
    public void test3() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance = Lists.newArrayList(85, 204, 128, 162, 222, 31, 207, 1, 52);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("RandomCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("EucledianDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(10, algorithm.getInitialK());
        Assert.assertTrue(algorithm.getFunctions().size() == 1);

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);

        Assert.assertEquals("SplitClustersFunction", function0.toString());

        List<Point> points = DataInstanceReader.readPoints("/prima-indians-diabetes.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();
        List<Point> points2 = clusteringContext.getPoints();
        for (Point p : points2) {
            System.out.println(p);
            System.out.println(p.getCluster());
            System.out.println();
        }

        // List<Cluster> clusters = clusteringContext.getClusters();
        // for (Cluster cluster : clusters) {
        // System.out.println(cluster.getCentroid());
        // System.out.println(cluster.getPoints().size());
        // System.out.println();
        // }
        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    /**
     * Another Test case that was being generated on algorithm with only
     * MoveNear and Split, and this was resulting in the end one cluster by
     * point with Fixed Points
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Test
    public void test4() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance =
            Lists.newArrayList(213, 151, 9, 37, 135, 209, 110, 46, 185, 180, 6, 1, 40, 172, 37, 103, 36);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("ManhattanDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(9, algorithm.getInitialK());
        Assert.assertEquals(2, algorithm.getFunctions().size());

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);

        Assert.assertEquals("MoveNearPointFunction", function0.toString());
        Assert.assertEquals("SplitClustersFunction", function1.toString());

        List<Point> points = DataInstanceReader.readPoints("/prima-indians-diabetes.data", "Double", true);
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

        System.out.println(clusteringContext.getClusters().size());

    }

    /**
     * Test case that was being generated on algorithm with only MoveNear and
     * Split, and this was resulting in the end one cluster by point
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Test
    public void test5() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance =
            Lists.newArrayList(213, 151, 9, 37, 135, 209, 110, 46, 185, 180, 6, 1, 40, 172, 37, 103, 36);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("ManhattanDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(9, algorithm.getInitialK());
        Assert.assertEquals(2, algorithm.getFunctions().size());

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);

        Assert.assertEquals("MoveNearPointFunction", function0.toString());
        Assert.assertEquals("SplitClustersFunction", function1.toString());

        ArrayList<Double> point1 = Lists.newArrayList(0.1, 0.2);
        ArrayList<Double> point2 = Lists.newArrayList(0.2, 0.1);
        ArrayList<Double> point3 = Lists.newArrayList(0.1, 0.3);
        ArrayList<Double> point4 = Lists.newArrayList(0.2, 0.2);
        ArrayList<Double> point5 = Lists.newArrayList(0.3, 0.2);
        ArrayList<Double> point6 = Lists.newArrayList(0.5, 0.4);
        ArrayList<Double> point7 = Lists.newArrayList(0.4, 0.4);
        ArrayList<Double> point8 = Lists.newArrayList(0.4, 0.5);
        ArrayList<Double> point9 = Lists.newArrayList(0.4, 0.6);
        ArrayList<Double> point10 = Lists.newArrayList(0.5, 0.5);
        ArrayList<Double> point11 = Lists.newArrayList(0.5, 0.6);
        ArrayList<Double> point12 = Lists.newArrayList(0.8, 0.8);
        ArrayList<Double> point13 = Lists.newArrayList(0.9, 0.8);
        ArrayList<Double> point14 = Lists.newArrayList(0.8, 0.9);
        ArrayList<Double> point15 = Lists.newArrayList(0.9, 0.9);
        ArrayList<Point> points = Lists.newArrayList(new Point(point1), new Point(point2), new Point(point3),
            new Point(point4), new Point(point5), new Point(point6), new Point(point7), new Point(point8),
            new Point(point9), new Point(point10), new Point(point11), new Point(point12), new Point(point13),
            new Point(point14), new Point(point15));
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

        for (Cluster cluster : clusteringContext.getClusters()) {
            cluster.printCluster();
        }

        System.out.println(clusteringContext.getClusters().size());

    }

    @Test
    public void test6() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance =
            Lists.newArrayList(129, 133, 233, 39, 53, 157, 209, 17, 69, 40, 192, 216, 33, 205, 233, 195, 171, 124);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);

        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("EucledianDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(10, algorithm.getInitialK());
        Assert.assertTrue(algorithm.getFunctions().size() == 4);

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);
        Function<ClusteringContext> function2 = algorithm.getFunctions().get(2);
        Function<ClusteringContext> function3 = algorithm.getFunctions().get(3);

        Assert.assertEquals("MoveAveragePointFunction", function0.toString());
        Assert.assertEquals("MoveAveragePointFunction", function1.toString());
        Assert.assertEquals("JoinClustersFunction", function2.toString());
        Assert.assertEquals("SplitClustersFunction", function3.toString());

        List<Point> points = DataInstanceReader.readPoints("/prima-indians-diabetes.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        List<Point> points2 = clusteringContext.getPoints();

        System.out.println(clusteringContext.getClusters().size());
        for (Cluster cluster : clusteringContext.getClusters()) {
            cluster.printCluster();
        }

        // List<Cluster> clusters = clusteringContext.getClusters();
        // for (Cluster cluster : clusters) {
        // System.out.println(cluster.getCentroid());
        // System.out.println(cluster.getPoints().size());
        // System.out.println();
        // }
        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    @Test
    public void test7() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance = Lists.newArrayList(41);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);

        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("ChebyshevDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(6, algorithm.getInitialK());
        Assert.assertEquals(400, algorithm.getFunctions().size());

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);
        Function<ClusteringContext> function2 = algorithm.getFunctions().get(2);
        Function<ClusteringContext> function3 = algorithm.getFunctions().get(3);

        Function<ClusteringContext> functionLast = algorithm.getFunctions().get(algorithm.getFunctions().size() - 1);

        Assert.assertEquals("SplitClustersFunction", function0.toString());
        Assert.assertEquals("SplitClustersFunction", function1.toString());

        Assert.assertEquals("MoveAveragePointFunction", functionLast.toString());

        List<Point> points = DataInstanceReader.readPoints("/points.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        List<Point> points2 = clusteringContext.getPoints();

        System.out.println(clusteringContext.getClusters().size());
        for (Cluster cluster : clusteringContext.getClusters()) {
            cluster.printCluster();
        }

        // List<Cluster> clusters = clusteringContext.getClusters();
        // for (Cluster cluster : clusters) {
        // System.out.println(cluster.getCentroid());
        // System.out.println(cluster.getPoints().size());
        // System.out.println();
        // }
        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    @Test
    public void test8() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance = Lists.newArrayList(7, 180, 6, 43, 101, 229, 225, 146, 217, 236, 245, 195, 238);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        //
        // Assert.assertEquals("UniformCentroidInitilization",
        // algorithm.getInitilization().toString());
        // Assert.assertEquals("ChebyshevDistanceFunction",
        // algorithm.getDistanceFunction().toString());
        //
        // Assert.assertEquals(6, algorithm.getInitialK());
        // Assert.assertEquals(400, algorithm.getFunctions().size());
        //
        // Function<ClusteringContext> function0 =
        // algorithm.getFunctions().get(0);
        // Function<ClusteringContext> function1 =
        // algorithm.getFunctions().get(1);
        // Function<ClusteringContext> function2 =
        // algorithm.getFunctions().get(2);
        // Function<ClusteringContext> function3 =
        // algorithm.getFunctions().get(3);
        //
        // Function<ClusteringContext> functionLast =
        // algorithm.getFunctions().get(algorithm.getFunctions().size() - 1);
        //
        // Assert.assertEquals("SplitClustersFunction", function0.toString());
        // Assert.assertEquals("SplitClustersFunction", function1.toString());

        // Assert.assertEquals("MoveAveragePointFunction",
        // functionLast.toString());

        List<Point> points = DataInstanceReader.readPoints("/points.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        List<Point> points2 = clusteringContext.getPoints();

        System.out.println(clusteringContext.getClusters().size());
        for (Cluster cluster : clusteringContext.getClusters()) {
            cluster.printCluster();
        }

        // List<Cluster> clusters = clusteringContext.getClusters();
        // for (Cluster cluster : clusters) {
        // System.out.println(cluster.getCentroid());
        // System.out.println(cluster.getPoints().size());
        // System.out.println();
        // }
        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    @Test
    public void test9() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance =
            Lists.newArrayList(108, 13, 119, 67, 85, 225, 205, 179, 61, 151, 9, 213, 85, 198, 87, 135, 90, 8, 246, 189);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        //
        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("ManhattanDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(9, algorithm.getInitialK());
        Assert.assertEquals(7, algorithm.getFunctions().size());

        // Function<ClusteringContext> function0 =
        // algorithm.getFunctions().get(0);
        // Function<ClusteringContext> function1 =
        // algorithm.getFunctions().get(1);
        // Function<ClusteringContext> function2 =
        // algorithm.getFunctions().get(2);
        // Function<ClusteringContext> function3 =
        // algorithm.getFunctions().get(3);
        //
        // Function<ClusteringContext> functionLast =
        // algorithm.getFunctions().get(algorithm.getFunctions().size() - 1);
        //
        // Assert.assertEquals("SplitClustersFunction", function0.toString());
        // Assert.assertEquals("SplitClustersFunction", function1.toString());

        // Assert.assertEquals("MoveAveragePointFunction",
        // functionLast.toString());

        List<Point> points = DataInstanceReader.readPoints("/points.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        List<Point> points2 = clusteringContext.getPoints();

        System.out.println(clusteringContext.getClusters().size());
        for (Cluster cluster : clusteringContext.getClusters()) {
            cluster.printCluster();
        }

        // List<Cluster> clusters = clusteringContext.getClusters();
        // for (Cluster cluster : clusters) {
        // System.out.println(cluster.getCentroid());
        // System.out.println(cluster.getPoints().size());
        // System.out.println();
        // }
        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    // 1,125,239,42,167,37,39,224,214,123,218,7,208,103,60,131

    @Test
    public void test10() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance = Lists.newArrayList(14, 243, 33, 38, 84);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);

        List<Point> points = DataInstanceReader.readPoints("/points.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        System.out.println(clusteringContext.getClusters().size());
        for (Cluster cluster : clusteringContext.getClusters()) {
            cluster.printCluster();
        }

        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    @Test
    public void test11() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance =
            Lists.newArrayList(1, 125, 239, 42, 167, 37, 7, 180, 6, 1, 40, 172, 178, 103, 36);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);

        ClusteringRandom.getInstance().setSeed(6);

        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("EucledianDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(9, algorithm.getInitialK());
        Assert.assertEquals(3, algorithm.getFunctions().size());

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);
        Function<ClusteringContext> function2 = algorithm.getFunctions().get(2);

        Assert.assertEquals("MoveAveragePointFunction", function0.toString());
        Assert.assertEquals("JoinClustersFunction", function1.toString());
        Assert.assertEquals("SplitClustersFunction", function2.toString());

        // Assert.assertEquals("MoveAveragePointFunction",
        // functionLast.toString());

        List<Point> points = DataInstanceReader.readPoints("/points.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        System.out.println(clusteringContext.getClusters().size());
        for (Cluster cluster : clusteringContext.getClusters()) {
            cluster.printCluster();
        }

        // List<Cluster> clusters = clusteringContext.getClusters();
        // for (Cluster cluster : clusters) {
        // System.out.println(cluster.getCentroid());
        // System.out.println(cluster.getPoints().size());
        // System.out.println();
        // }
        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

    @Test
    public void test11MultipleSeeds() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance =
            Lists.newArrayList(1, 125, 239, 42, 167, 37, 7, 180, 6, 1, 40, 172, 178, 103, 36);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);

        List<Point> points = DataInstanceReader.readPoints("/points.data", "Double", true);
        algorithm.setPoints(points);

        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("EucledianDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(9, algorithm.getInitialK());
        Assert.assertEquals(3, algorithm.getFunctions().size());

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);
        Function<ClusteringContext> function2 = algorithm.getFunctions().get(2);

        Assert.assertEquals("MoveAveragePointFunction", function0.toString());
        Assert.assertEquals("JoinClustersFunction", function1.toString());
        Assert.assertEquals("SplitClustersFunction", function2.toString());

        List<Double> fitnesses = new ArrayList<Double>();
        Map<Integer, Integer> mapQtdClusters = new HashMap<>();

        for (int i = 0; i < 30; i++) {
            System.out.println("############################################################");
            System.out.println("Seed :" + i);
            ClusteringRandom.getNewInstance().setSeed(i);

            ClusteringContext clusteringContext = algorithm.execute();

            int clustersSize = clusteringContext.getClusters().size();
            System.out.println("Clusters size: " + clustersSize);
            for (Cluster cluster : clusteringContext.getClusters()) {
                cluster.printCluster();
            }
            Double fitness = fitnessFunction.apply(clusteringContext);

            System.out.println("Fitness: " + fitness);
            fitnesses.add(fitness);

            algorithm.clearClusteringContext();

            if (!mapQtdClusters.containsKey(clustersSize)) {
                mapQtdClusters.put(clustersSize, 1);
            } else {
                Integer amount = mapQtdClusters.get(clustersSize);
                mapQtdClusters.put(clustersSize, amount + 1);
            }
            System.out.println("############################################################");
        }

        List<Double> filteredFitnesses = fitnesses.stream().filter(f -> {
            if (f != Double.MAX_VALUE)
                return true;
            return false;
        }).collect(Collectors.toList());

        DoubleSummaryStatistics summaryStatistics =
            filteredFitnesses.stream().collect(Collectors.summarizingDouble(x -> x));

        System.out.println("Clusters size:");
        Set<Integer> qtdClusters = mapQtdClusters.keySet();
        for (Integer qtd : qtdClusters) {
            System.out.println(qtd + ":" + mapQtdClusters.get(qtd));
        }

        System.out.println("Average: " + summaryStatistics.getAverage());
        System.out.println("Min: " + summaryStatistics.getMin());
        System.out.println("Max: " + summaryStatistics.getMax());
        System.out.println("Count: " + summaryStatistics.getCount());
        System.out.println("Std: " + MathUtils.getStdDev(filteredFitnesses, summaryStatistics.getAverage()));

    }

    @Test
    public void test12MultipleSeeds() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance =
            Lists.newArrayList(1, 125, 239, 42, 167, 37, 7, 180, 6, 1, 40, 172, 178, 103, 36);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);

        List<Point> points = DataInstanceReader.readPoints("/morePoints.data", "Double", true);
        ;
        algorithm.setPoints(points);

        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("EucledianDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(9, algorithm.getInitialK());
        Assert.assertEquals(3, algorithm.getFunctions().size());

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);
        Function<ClusteringContext> function2 = algorithm.getFunctions().get(2);

        Assert.assertEquals("MoveAveragePointFunction", function0.toString());
        Assert.assertEquals("JoinClustersFunction", function1.toString());
        Assert.assertEquals("SplitClustersFunction", function2.toString());

        // Assert.assertEquals("MoveAveragePointFunction",
        // functionLast.toString());

        List<Double> fitnesses = new ArrayList<Double>();

        for (int i = 0; i < 30; i++) {

            System.out.println("Seed :" + i);
            ClusteringRandom.getNewInstance().setSeed(i);

            ClusteringContext clusteringContext = algorithm.execute();

            // System.out.println(clusteringContext.getClusters().size());
            // for (Cluster cluster : clusteringContext.getClusters()) {
            // cluster.printCluster();
            // }
            Double fitness = fitnessFunction.apply(clusteringContext);
            System.out.println(fitness);
            fitnesses.add(fitness);

            algorithm.clearClusteringContext();
        }

        List<Double> filteredFitnesses = fitnesses.stream().filter(f -> {
            if (f != Double.MAX_VALUE)
                return true;
            return false;
        }).collect(Collectors.toList());

        DoubleSummaryStatistics summaryStatistics =
            filteredFitnesses.stream().collect(Collectors.summarizingDouble(x -> x));

        System.out.println("Average: " + summaryStatistics.getAverage());

    }

    @Test
    public void test50RandomPoints30Seeds() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance =
            Lists.newArrayList(1, 125, 239, 42, 167, 37, 7, 180, 6, 1, 40, 172, 178, 103, 36);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);

        List<Point> points = DataInstanceReader.readPoints("/50-random-points.data", "Double", true);
        algorithm.setPoints(points);

        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("EucledianDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(9, algorithm.getInitialK());
        Assert.assertEquals(3, algorithm.getFunctions().size());

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);
        Function<ClusteringContext> function2 = algorithm.getFunctions().get(2);

        Assert.assertEquals("MoveAveragePointFunction", function0.toString());
        Assert.assertEquals("JoinClustersFunction", function1.toString());
        Assert.assertEquals("SplitClustersFunction", function2.toString());

        // Assert.assertEquals("MoveAveragePointFunction",
        // functionLast.toString());

        List<Double> fitnesses = new ArrayList<Double>();
        Map<Integer, Integer> mapQtdClusters = new HashMap<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 30; i++) {

            System.out.println("Seed :" + i);
            ClusteringRandom.getNewInstance().setSeed(i);

            ClusteringContext clusteringContext = algorithm.execute();

            // System.out.println(clusteringContext.getClusters().size());
            // for (Cluster cluster : clusteringContext.getClusters()) {
            // cluster.printCluster();
            // }
            Double fitness = fitnessFunction.apply(clusteringContext);
            System.out.println(fitness);
            fitnesses.add(fitness);

            int clustersSize = clusteringContext.getClusters().size();
            if (!mapQtdClusters.containsKey(clustersSize)) {
                mapQtdClusters.put(clustersSize, 1);
            } else {
                Integer amount = mapQtdClusters.get(clustersSize);
                mapQtdClusters.put(clustersSize, amount + 1);
            }

            algorithm.clearClusteringContext();
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

        System.out.println("Clusters size:");
        Set<Integer> qtdClusters = mapQtdClusters.keySet();
        for (Integer qtd : qtdClusters) {
            System.out.println(qtd + ":" + mapQtdClusters.get(qtd));
        }

        System.out.println("Average: " + summaryStatistics.getAverage());
        System.out.println("Min: " + summaryStatistics.getMin());
        System.out.println("Max: " + summaryStatistics.getMax());
        System.out.println("Count: " + summaryStatistics.getCount());
        System.out.println("Std: " + MathUtils.getStdDev(filteredFitnesses, summaryStatistics.getAverage()));

    }

    @Test
    public void test500RandomPoints30Seeds() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance =
            Lists.newArrayList(1, 125, 239, 42, 167, 37, 7, 180, 6, 1, 40, 172, 178, 103, 36);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);

        List<Point> points = DataInstanceReader.readPoints("/500-random-points.data", "Double", true);
        algorithm.setPoints(points);

        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("EucledianDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(9, algorithm.getInitialK());
        Assert.assertEquals(3, algorithm.getFunctions().size());

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);
        Function<ClusteringContext> function2 = algorithm.getFunctions().get(2);

        Assert.assertEquals("MoveAveragePointFunction", function0.toString());
        Assert.assertEquals("JoinClustersFunction", function1.toString());
        Assert.assertEquals("SplitClustersFunction", function2.toString());

        // Assert.assertEquals("MoveAveragePointFunction",
        // functionLast.toString());

        List<Double> fitnesses = new ArrayList<Double>();
        Map<Integer, Integer> mapQtdClusters = new HashMap<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 30; i++) {

            System.out.println("Seed :" + i);
            ClusteringRandom.getNewInstance().setSeed(i);

            ClusteringContext clusteringContext = algorithm.execute();

            // System.out.println(clusteringContext.getClusters().size());
            // for (Cluster cluster : clusteringContext.getClusters()) {
            // cluster.printCluster();
            // }
            Double fitness = fitnessFunction.apply(clusteringContext);
            System.out.println(fitness);
            fitnesses.add(fitness);

            int clustersSize = clusteringContext.getClusters().size();
            if (!mapQtdClusters.containsKey(clustersSize)) {
                mapQtdClusters.put(clustersSize, 1);
            } else {
                Integer amount = mapQtdClusters.get(clustersSize);
                mapQtdClusters.put(clustersSize, amount + 1);
            }

            algorithm.clearClusteringContext();
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

        System.out.println("Clusters size:");
        Set<Integer> qtdClusters = mapQtdClusters.keySet();
        for (Integer qtd : qtdClusters) {
            System.out.println(qtd + ":" + mapQtdClusters.get(qtd));
        }

        System.out.println("Average: " + summaryStatistics.getAverage());
        System.out.println("Min: " + summaryStatistics.getMin());
        System.out.println("Max: " + summaryStatistics.getMax());
        System.out.println("Count: " + summaryStatistics.getCount());
        System.out.println("Std: " + MathUtils.getStdDev(filteredFitnesses, summaryStatistics.getAverage()));

    }

    @Test
    public void test1000RandomPoints30Seeds() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance =
            Lists.newArrayList(1, 125, 239, 42, 167, 37, 7, 180, 6, 1, 40, 172, 178, 103, 36);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);

        List<Point> points = DataInstanceReader.readPoints("/1000-random-points.data", "Double", true);
        algorithm.setPoints(points);

        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("EucledianDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(9, algorithm.getInitialK());
        Assert.assertEquals(3, algorithm.getFunctions().size());

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);
        Function<ClusteringContext> function2 = algorithm.getFunctions().get(2);

        Assert.assertEquals("MoveAveragePointFunction", function0.toString());
        Assert.assertEquals("JoinClustersFunction", function1.toString());
        Assert.assertEquals("SplitClustersFunction", function2.toString());

        // Assert.assertEquals("MoveAveragePointFunction",
        // functionLast.toString());

        List<Double> fitnesses = new ArrayList<Double>();
        Map<Integer, Integer> mapQtdClusters = new HashMap<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 30; i++) {

            System.out.println("Seed :" + i);
            ClusteringRandom.getNewInstance().setSeed(i);

            ClusteringContext clusteringContext = algorithm.execute();

            // System.out.println(clusteringContext.getClusters().size());
            // for (Cluster cluster : clusteringContext.getClusters()) {
            // cluster.printCluster();
            // }
            Double fitness = fitnessFunction.apply(clusteringContext);
            System.out.println(fitness);
            fitnesses.add(fitness);

            int clustersSize = clusteringContext.getClusters().size();
            if (!mapQtdClusters.containsKey(clustersSize)) {
                mapQtdClusters.put(clustersSize, 1);
            } else {
                Integer amount = mapQtdClusters.get(clustersSize);
                mapQtdClusters.put(clustersSize, amount + 1);
            }

            algorithm.clearClusteringContext();
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

        System.out.println("Clusters size:");
        Set<Integer> qtdClusters = mapQtdClusters.keySet();
        for (Integer qtd : qtdClusters) {
            System.out.println(qtd + ":" + mapQtdClusters.get(qtd));
        }

        System.out.println("Average: " + summaryStatistics.getAverage());
        System.out.println("Min: " + summaryStatistics.getMin());
        System.out.println("Max: " + summaryStatistics.getMax());
        System.out.println("Count: " + summaryStatistics.getCount());
        System.out.println("Std: " + MathUtils.getStdDev(filteredFitnesses, summaryStatistics.getAverage()));
    }

    @Test
    public void testDiabetesRandomPoints30Seeds() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance =
            Lists.newArrayList(1, 125, 239, 42, 167, 37, 7, 180, 6, 1, 40, 172, 178, 103, 36);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);

        List<Point> points =
            MathUtils.normalizeData(DataInstanceReader.readPoints("/prima-indians-diabetes.data", "Double", true));
        algorithm.setPoints(points);

        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("EucledianDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(9, algorithm.getInitialK());
        Assert.assertEquals(3, algorithm.getFunctions().size());

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);
        Function<ClusteringContext> function2 = algorithm.getFunctions().get(2);

        Assert.assertEquals("MoveAveragePointFunction", function0.toString());
        Assert.assertEquals("JoinClustersFunction", function1.toString());
        Assert.assertEquals("SplitClustersFunction", function2.toString());

        // Assert.assertEquals("MoveAveragePointFunction",
        // functionLast.toString());

        List<Double> fitnesses = new ArrayList<Double>();
        Map<Integer, Integer> mapQtdClusters = new HashMap<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 30; i++) {

            System.out.println("Seed :" + i);
            ClusteringRandom.getNewInstance().setSeed(i);

            ClusteringContext clusteringContext = algorithm.execute();

            // System.out.println(clusteringContext.getClusters().size());
            // for (Cluster cluster : clusteringContext.getClusters()) {
            // cluster.printCluster();
            // }
            Double fitness = fitnessFunction.apply(clusteringContext);
            System.out.println(fitness);
            fitnesses.add(fitness);

            int clustersSize = clusteringContext.getClusters().size();
            if (!mapQtdClusters.containsKey(clustersSize)) {
                mapQtdClusters.put(clustersSize, 1);
            } else {
                Integer amount = mapQtdClusters.get(clustersSize);
                mapQtdClusters.put(clustersSize, amount + 1);
            }

            algorithm.clearClusteringContext();
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

        System.out.println("Clusters size:");
        Set<Integer> qtdClusters = mapQtdClusters.keySet();
        for (Integer qtd : qtdClusters) {
            System.out.println(qtd + ":" + mapQtdClusters.get(qtd));
        }

        System.out.println("Average: " + summaryStatistics.getAverage());
        System.out.println("Min: " + summaryStatistics.getMin());
        System.out.println("Max: " + summaryStatistics.getMax());
        System.out.println("Count: " + summaryStatistics.getCount());
        System.out.println("Std: " + MathUtils.getStdDev(filteredFitnesses, summaryStatistics.getAverage()));
    }

    @Test
    public void test12() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance =
            Lists.newArrayList(1, 125, 239, 42, 167, 37, 7, 180, 6, 1, 40, 172, 178, 103, 36);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);

        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("EucledianDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(10, algorithm.getInitialK());
        Assert.assertEquals(3, algorithm.getFunctions().size());

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);
        Function<ClusteringContext> function2 = algorithm.getFunctions().get(2);

        Assert.assertEquals("MoveAveragePointFunction", function0.toString());
        Assert.assertEquals("JoinClustersFunction", function1.toString());
        Assert.assertEquals("SplitClustersFunction", function2.toString());

        // Assert.assertEquals("MoveAveragePointFunction",
        // functionLast.toString());

        List<Point> points = DataInstanceReader.readPoints("/morePoints.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext clusteringContext = algorithm.execute();

        System.out.println(clusteringContext.getClusters().size());
        for (Cluster cluster : clusteringContext.getClusters()) {
            cluster.printCluster();
        }

        // List<Cluster> clusters = clusteringContext.getClusters();
        // for (Cluster cluster : clusters) {
        // System.out.println(cluster.getCentroid());
        // System.out.println(cluster.getPoints().size());
        // System.out.println();
        // }
        Double fitness = fitnessFunction.apply(clusteringContext);

        System.out.println(fitness);

    }

}
