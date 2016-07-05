package edu.ufpr.jmetal.problem.functions;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import edu.ufpr.cluster.algorithm.Cluster;
import edu.ufpr.cluster.algorithm.ClusteringContext;
import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.cluster.algorithms.functions.DistanceFunction;
import edu.ufpr.cluster.algorithms.functions.impl.EucledianDistanceFunction;
import edu.ufpr.jmetal.problem.fitness.FitnessFunction;
import edu.ufpr.jmetal.problem.fitness.SilhouetteFitness;

public class SilhouetteFitnessTest {

	private FitnessFunction fitness;
	private DistanceFunction distance;
	
	@Before
	public void setup() {
		fitness = new SilhouetteFitness();
		distance = new EucledianDistanceFunction();
	} 
	
	@Test
	public void testFullCluster() {
		
		Point p0 = new Point(Lists.newArrayList(1.0, 1.0));
		Point p1 = new Point(Lists.newArrayList(2.0, 2.0));
		Point p2 = new Point(Lists.newArrayList(3.0, 3.0));
		Point p3 = new Point(Lists.newArrayList(4.0, 4.0));
		Point p4 = new Point(Lists.newArrayList(5.0, 5.0));
		
		List<Point> points = new ArrayList<>();
		points.add(p0);
		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);
		
		Cluster c0 = new Cluster();
		c0.addPoint(p0);
		c0.addPoint(p1);
		c0.addPoint(p2);
		c0.addPoint(p3);
		c0.addPoint(p4);
		c0.updateCentroid();
		
		List<Cluster> clusters = new ArrayList<>();
		clusters.add(c0);
		
		ClusteringContext context = new ClusteringContext(points, clusters, distance);
		
		Double f = fitness.apply(context);
		
		Assert.assertEquals(-1.0, f, 0.0);
		
		System.out.println(f);
	}
	
	@Test
	public void testOnePointCluster() {
		
		Point p0 = new Point(Lists.newArrayList(1.0, 1.0));
		Point p1 = new Point(Lists.newArrayList(2.0, 2.0));
		Point p2 = new Point(Lists.newArrayList(3.0, 3.0));
		
		List<Point> points = new ArrayList<>();
		points.add(p0);
		points.add(p1);
		points.add(p2);
		
		Cluster c0 = new Cluster();
		Cluster c1 = new Cluster();
		Cluster c2 = new Cluster();
		
		c0.addPoint(p0);
		c0.updateCentroid();
		
		c1.addPoint(p1);
		c1.updateCentroid();
		
		c2.addPoint(p2);
		c2.updateCentroid();
		
		List<Cluster> clusters = new ArrayList<>();
		clusters.add(c0);
		clusters.add(c1);
		clusters.add(c2);
		
		ClusteringContext context = new ClusteringContext(points, clusters, distance);
		
		Double f = fitness.apply(context);
		
		Assert.assertEquals(0.0, f, 0.0);
		
		System.out.println(f);
	}
	
	@Test
	public void testOneClusterAlone() {
		
		Point p0 = new Point(Lists.newArrayList(1.0, 1.0));
		Point p1 = new Point(Lists.newArrayList(2.0, 2.0));
		Point p2 = new Point(Lists.newArrayList(3.0, 3.0));
		Point p3 = new Point(Lists.newArrayList(4.0, 4.0));
		Point p4 = new Point(Lists.newArrayList(5.0, 5.0));
		
		List<Point> points = new ArrayList<>();
		points.add(p0);
		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);
		
		Cluster c0 = new Cluster();
		Cluster c1 = new Cluster();
		Cluster c2 = new Cluster();
		
		c0.addPoint(p0);
		c0.updateCentroid();
		
		c1.addPoint(p1);
		c1.addPoint(p2);
		c1.updateCentroid();
		
		c2.addPoint(p3);
		c2.addPoint(p4);
		c2.updateCentroid();
		
		List<Cluster> clusters = new ArrayList<>();
		clusters.add(c0);
		clusters.add(c1);
		clusters.add(c2);
		
		ClusteringContext context = new ClusteringContext(points, clusters, distance);
		
		Double f = fitness.apply(context);
		
		System.out.println(f);
	}
}
