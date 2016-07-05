package edu.ufpr.cluster.algorithms.functions.impl;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import edu.ufpr.cluster.algorithm.Cluster;
import edu.ufpr.cluster.algorithm.ClusteringContext;
import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.cluster.algorithms.functions.DistanceFunction;

public class MoveBetweenClustersTest {

	private MoveBetweenClustersFunction function;

	@Before
	public void init() {
		function = new MoveBetweenClustersFunction();
		JMetalRandom.getInstance().setSeed(111);
	}

	@Test
	public void test() {

		DistanceFunction distanceFunction = new EucledianDistanceFunction();
		int k = 4;

		List<Point> points = new ArrayList<Point>();

		Point point0 = new Point(Lists.newArrayList(1.0, 0.0));
		Point point1 = new Point(Lists.newArrayList(1.0, 5.0));
		Point point2 = new Point(Lists.newArrayList(2.0, 0.0));
		Point point3 = new Point(Lists.newArrayList(3.0, 0.0));
		Point point4 = new Point(Lists.newArrayList(4.0, 0.0));
		Point point5 = new Point(Lists.newArrayList(5.0, 0.0));
		Point point6 = new Point(Lists.newArrayList(6.0, 0.0));

		points.add(point0);
		points.add(point1);
		points.add(point2);
		points.add(point3);
		points.add(point4);
		points.add(point5);
		points.add(point6);

		Cluster cluster0 = new Cluster();
		Cluster cluster1 = new Cluster();
		Cluster cluster2 = new Cluster();

		cluster0.addPoint(point0);
		cluster0.addPoint(point1);
		cluster0.updateCentroid();

		cluster1.addPoint(point2);
		cluster1.addPoint(point3);
		cluster1.addPoint(point4);
		cluster1.updateCentroid();

		cluster2.addPoint(point5);
		cluster2.addPoint(point6);
		cluster2.updateCentroid();

		List<Cluster> clusters = new ArrayList<Cluster>();
		clusters.add(cluster0);
		clusters.add(cluster1);
		clusters.add(cluster2);

		ClusteringContext clusteringContext = new ClusteringContext(points, clusters, distanceFunction);

		for (Cluster cluster : clusters) {
			System.out.println(cluster.getPoints().size() + " " + cluster);
		}

		function.apply(clusteringContext);

		System.out.println("---");
		for (Cluster cluster : clusters) {
			System.out.println(cluster.getPoints().size() + " " + cluster);
		}

	}

}
