/*
 * Copyright 2015, Charter Communications, All rights reserved.
 */
package edu.ufpr.ge.mapper.test;

import com.google.common.collect.Lists;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import edu.ufpr.cluster.algorithm.Cluster;
import edu.ufpr.cluster.algorithm.ClusteringAlgorithm;
import edu.ufpr.cluster.algorithm.ClusteringContext;
import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.cluster.algorithms.functions.Function;
import edu.ufpr.ge.mapper.impl.ClusteringExpressionGrammarMapper;
import edu.ufpr.jmetal.problem.old.impl.DataInstanceReader;

/**
 *
 *
 * @author Vidal
 */
public class ClusteringExpressionGrammarMapperTest {

    private ClusteringExpressionGrammarMapper mapper;

    @Before
    public void setup() {

        mapper = new ClusteringExpressionGrammarMapper(20);
        mapper.loadGrammar("/clustergrammar.bnf");
        JMetalRandom.getInstance().setSeed(100);
    }

    /**
     * <pre>
     * 7, 180, 6, 1, 40, 172, 132, 103, 36
     * 0,  0,  1, 1,  0,   2          -> Mod
     * </pre>
     * 
     * RandomCentroidInitilization 6 ManhattanDistanceFunction
     * MoveAveragePointFunction
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    @Test
    public void test0() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance = Lists.newArrayList(7, 180, 6, 1, 40, 172, 132, 103, 36);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("RandomCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("ManhattanDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(6, algorithm.getInitialK());
        Assert.assertTrue(algorithm.getFunctions().size() == 1);
        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Assert.assertEquals("MoveAveragePointFunction", function0.toString());

        List<Point> points = DataInstanceReader.readPoints("/prima-indians-diabetes.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext execute = algorithm.execute();
        System.out.println("Clusters: " + execute.getClusters().size());

        List<Cluster> clusters = execute.getClusters();
        for (Cluster cluster : clusters) {
            System.out.println(cluster.getCentroid());
            System.out.println(cluster.getPoints().size());
            System.out.println();
        }

    }

    /**
     * <pre>
     *	1,125,239,42,167,37,39,224,214,123,218,7,208,103,60,131,
     *	0, 1,  4,  0,  1, 2, 1,  4,  0,  3,
     * </pre>
     * 
     * UniformCentroidInitilization 0->9 EucledianDistanceFunction
     * 
     * MoveAveragePointFunction MoveNearPointFunction
     * MoveBetweenClustersFunction
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    @Test
    public void test1() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance =
            Lists.newArrayList(1, 125, 239, 42, 167, 37, 39, 224, 214, 123, 218, 7, 208, 103, 60, 131);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("UniformCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("EucledianDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(9, algorithm.getInitialK());
        Assert.assertTrue(algorithm.getFunctions().size() == 3);

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);
        Function<ClusteringContext> function2 = algorithm.getFunctions().get(2);

        Assert.assertEquals("MoveAveragePointFunction", function0.toString());
        Assert.assertEquals("MoveNearPointFunction", function1.toString());
        Assert.assertEquals("MoveBetweenClustersFunction", function2.toString());

        List<Point> points = DataInstanceReader.readPoints("/prima-indians-diabetes.data", "Double", true);
        algorithm.setPoints(points);

        algorithm.execute();

    }

    /**
     * <pre>
     *	24,120,154,179,66,23,18,32,99,43,101,229,225,146,217,124,245,195,238,
     *	0,  0, 	 4,  2, 0, 3,
     * 
     * </pre>
     * 
     * RandomCentroidInitilization 9 ChebyshevDistanceFunction
     * MoveBetweenClustersFunction
     */
    @Test
    public void test2() {

        List<Integer> grammarInstance =
            Lists.newArrayList(24, 120, 154, 179, 66, 23, 18, 32, 99, 43, 101, 229, 225, 146, 217, 124, 245, 195, 238);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("RandomCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("ChebyshevDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(9, algorithm.getInitialK());
        Assert.assertTrue(algorithm.getFunctions().size() == 1);

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);

        Assert.assertEquals("MoveBetweenClustersFunction", function0.toString());

    }

    /**
     * <pre>
     *	85,204,128,162,222,31,207,1,52,
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

        ClusteringContext execute = algorithm.execute();
        System.out.println("Clusters: " + execute.getClusters().size());

    }

    /**
     * <pre>
     *	85,204,108,223,155,223,155,
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
    public void test4() throws FileNotFoundException, IOException {

        List<Integer> grammarInstance = Lists.newArrayList(85, 204, 108, 223, 155, 223, 155);

        ClusteringAlgorithm algorithm = mapper.interpret(grammarInstance);
        Assert.assertEquals("RandomCentroidInitilization", algorithm.getInitilization().toString());
        Assert.assertEquals("ManhattanDistanceFunction", algorithm.getDistanceFunction().toString());

        Assert.assertEquals(10, algorithm.getInitialK());

        Assert.assertTrue(algorithm.getFunctions().size() == 3);

        Function<ClusteringContext> function0 = algorithm.getFunctions().get(0);
        Function<ClusteringContext> function1 = algorithm.getFunctions().get(1);
        Function<ClusteringContext> function2 = algorithm.getFunctions().get(2);

        Assert.assertEquals("MoveBetweenClustersFunction", function0.toString());
        Assert.assertEquals("JoinClustersFunction", function1.toString());
        Assert.assertEquals("MoveBetweenClustersFunction", function2.toString());

        List<Point> points = DataInstanceReader.readPoints("/prima-indians-diabetes.data", "Double", true);
        algorithm.setPoints(points);

        ClusteringContext execute = algorithm.execute();
        System.out.println("Clusters: " + execute.getClusters().size());

    }
}
