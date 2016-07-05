package edu.ufpr.cluster.algorithms.functions.impl;

import java.util.ArrayList;
import java.util.List;

import edu.ufpr.cluster.algorithm.Cluster;
import edu.ufpr.cluster.algorithm.ClusteringContext;
import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.cluster.algorithms.functions.Function;
import edu.ufpr.cluster.random.ClusteringRandom;

public class MoveNearPointFunction implements Function<ClusteringContext> {

	@Override
	public void apply(ClusteringContext context) {

		int r = ClusteringRandom.getInstance().nextInt(0, context.getPoints().size() - 1);
		Point p = context.getPoints().get(r);
		Cluster c = p.getCluster();

		Cluster minCluster = context.getClusters().get(0);
		double minDistance = Double.MAX_VALUE;

		for (Cluster cluster : context.getClusters()) {
			if (cluster != c) {

				List<Point> points = new ArrayList<Point>();
				points.add(p);
				points.add(cluster.getCentroid());

				double dist = context.getDistanceFunction().apply(points);

				if (dist < minDistance) {
					minDistance = dist;
					minCluster = cluster;
				}
			}
		}

		if (!minCluster.getPoints().contains(p)) {
			minCluster.addPoint(p);
			minCluster.updateCentroid();

			// Only Removes the point from the original code. If the point
			// really had a original cluster
			if (c != null) {
				c.getPoints().remove(p);
				if (!c.isEmpty())
					c.updateCentroid();
				else
					context.getClusters().remove(c);
			}
		}

	}

	@Override
	public String toString() {
		return "MoveNearPointFunction";
	}

}
