package edu.ufpr.cluster.algorithms.functions.impl;

import com.google.common.collect.Lists;

import edu.ufpr.cluster.algorithm.Cluster;
import edu.ufpr.cluster.algorithm.ClusteringContext;
import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.cluster.algorithms.functions.Function;
import edu.ufpr.cluster.random.ClusteringRandom;

public class MoveAveragePointFunction implements Function<ClusteringContext> {

	@Override
	public void apply(ClusteringContext context) {

		int r = ClusteringRandom.getInstance().nextInt(0, context.getPoints().size() - 1);
		Point p = context.getPoints().get(r);
		Cluster c = p.getCluster();

		Cluster minCluster = context.getClusters().get(0);
		double minDistance = Double.MAX_VALUE;

		for (Cluster cluster : context.getClusters()) {
			if (cluster != c) {

				double sum = 0.0;
				for (Point q : cluster.getPoints()) {
					sum += context.getDistanceFunction().apply(Lists.newArrayList(p, q));
				}

				sum = sum / cluster.getPoints().size();

				if (sum < minDistance) {
					minDistance = sum;
					minCluster = cluster;
				}
			}
		}

		if (!minCluster.getPoints().contains(p)) {
			if (c != null) {
				c.getPoints().remove(p);
				if (!c.isEmpty())
					c.updateCentroid();
				else
					context.getClusters().remove(c);
			}
			minCluster.addPoint(p);
			minCluster.updateCentroid();
		}

	}

	@Override
	public String toString() {
		return "MoveAveragePointFunction";
	}

}
