package edu.ufpr.cluster.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ClusteringContext {

	private Function<List<Point>, Double> distanceFunction;

	private List<Cluster> clusters;

	private List<Point> points;

	private int dimensions;

    private int evaluationsCount = 0;

	public ClusteringContext(List<Point> points, Function<List<Point>, Double> distanceFunction) {
		setup(points, null, distanceFunction);
	}

	public ClusteringContext(List<Point> points, List<Cluster> clusters,
			Function<List<Point>, Double> distanceFunction) {
		setup(points, clusters, distanceFunction);
	}

	public void setup(List<Point> points, List<Cluster> clusters, Function<List<Point>, Double> distanceFunction) {
		this.clusters = new ArrayList<>();
		this.points = points;
		this.clusters = clusters;
		this.distanceFunction = distanceFunction;

		int lastSize = points.get(0).getCoordinates().size();
		for (Point point : points) {
			int size = point.getCoordinates().size();
			if (lastSize != size) {
				throw new IllegalArgumentException("All points should have the same dimensions");
			}
			lastSize = size;
		}
		this.dimensions = lastSize;
	}

	public List<Cluster> getClusters() {
		if (clusters == null) {
			clusters = new ArrayList<Cluster>();
		}
		return clusters;
	}

	public void clearClustersState() {
		for (Cluster cluster : clusters) {
			cluster.getPoints().clear();
		}
        setEvaluationsCount(0);
	}

	public void setClusters(List<Cluster> clusters) {
		this.clusters = clusters;
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}

	public Function<List<Point>, Double> getDistanceFunction() {
		return distanceFunction;
	}

	public void setDistanceFunction(Function<List<Point>, Double> distanceFunction) {
		this.distanceFunction = distanceFunction;
	}

	public int getDimensions() {
		return dimensions;
	}

	public void setDimensions(int dimensions) {
		this.dimensions = dimensions;
	}

    public int getEvaluationsCount() {

        return evaluationsCount;
    }

    public void setEvaluationsCount(int evaluationsCount) {

        this.evaluationsCount = evaluationsCount;
    }

}
