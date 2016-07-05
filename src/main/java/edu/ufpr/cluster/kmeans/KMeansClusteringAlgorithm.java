package edu.ufpr.cluster.kmeans;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.ufpr.cluster.algorithm.Cluster;
import edu.ufpr.cluster.algorithm.ClusteringContext;
import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.cluster.algorithms.functions.DistanceFunction;
import edu.ufpr.cluster.random.ClusteringRandom;

/**
 *
 *
 * @author Vidal
 */
public class KMeansClusteringAlgorithm {

	private List<Point> points;
	private DistanceFunction distanceFunction;
	private int k;
	private int numCoordinates;

	private ClusteringContext clusteringContext;

	public KMeansClusteringAlgorithm(List<Point> points, DistanceFunction distanceFunction, int k, int numCoordinates) {
		this.points = points;
		this.distanceFunction = distanceFunction;
		this.k = k;
		this.numCoordinates = numCoordinates;

	}

	public ClusteringContext execute() {

		initialization();

		calculate();

		return clusteringContext;
	}

	public void calculate() {
		boolean finish = false;
		int iteration = 0;

		// Add in new data, one at a time, recalculating centroids with each new
		// one.
		while (!finish) {

			// Clear cluster state
			clusteringContext.clearClustersState();

			List<Point> lastCentroids = clusteringContext.getClusters().stream().map(c -> c.getCentroid())
					.collect(Collectors.toList());

			// Assign points to the closer cluster
			assignCluster();

			// Calculate new centroids.
			clusteringContext.getClusters().stream().forEach(c -> c.updateCentroid());

			iteration++;

			List<Point> currentCentroids = clusteringContext.getClusters().stream().map(c -> c.getCentroid())
					.collect(Collectors.toList());

			// Calculates total distance between new and old Centroids
			double distance = 0;
			for (int i = 0; i < lastCentroids.size(); i++) {
				Point last = lastCentroids.get(i);
				Point current = currentCentroids.get(i);
				distance += distanceFunction.apply(Lists.newArrayList(last, current));
			}

			if (distance == 0) {
				finish = true;
			}

		}
	}

	public void initialization() {

		clusteringContext = new ClusteringContext(points, distanceFunction);

		for (int i = 0; i < k; i++) {
			Cluster cluster = new Cluster();
			// Point centroid = createRandomPoint(numCoordinates);
			Point centroid = null;
			do {
				centroid = chooseRandomPoint(points);
			} while (getCentroids().contains(centroid));

			cluster.setCentroid(centroid);
			clusteringContext.getClusters().add(cluster);
		}

		System.out.println("Initial centroid");
		clusteringContext.getClusters().stream().forEach(c -> {
			System.out.println(c.getCentroid());
		});
	}

	public List<Point> getCentroids() {
		return clusteringContext.getClusters().stream().map(c -> c.getCentroid()).collect(Collectors.toList());
	}

	private void assignCluster() {
		double max = Double.MAX_VALUE;
		double min = max;
		Cluster cluster = null;
		double distance = 0.0;

		for (Point point : points) {
			min = max;
			for (int i = 0; i < k; i++) {
				Cluster c = clusteringContext.getClusters().get(i);

				Point centroidPoint = c.getCentroid();
				distance = distanceFunction.apply(Lists.newArrayList(point, centroidPoint));
				if (distance < min) {
					min = distance;
					cluster = c;
				}
			}
			point.setCluster(cluster);
			cluster.addPoint(point);
		}
	}

	protected static Point createRandomPoint(int numCoordinates) {

		List<Double> coordinates = new ArrayList<Double>();
		for (int i = 0; i < numCoordinates; i++) {
			coordinates.add(ClusteringRandom.getInstance().nextDouble());
		}
		return new Point(coordinates);
	}

	public void clearClusteringContext() {
		clusteringContext.setClusters(null);
		clusteringContext.getPoints().stream().forEach(p -> p.setCluster(null));
	}

	protected static Point chooseRandomPoint(List<Point> points) {

		int index = ClusteringRandom.getInstance().nextInt(0, points.size() - 1);
		Point point = points.get(index).copy();
		return point;
	}

}
