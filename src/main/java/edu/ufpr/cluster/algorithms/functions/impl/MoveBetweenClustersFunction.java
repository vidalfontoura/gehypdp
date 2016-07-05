package edu.ufpr.cluster.algorithms.functions.impl;

import com.google.common.collect.Lists;

import edu.ufpr.cluster.algorithm.Cluster;
import edu.ufpr.cluster.algorithm.ClusteringContext;
import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.cluster.algorithms.functions.Function;
import edu.ufpr.cluster.random.ClusteringRandom;

public class MoveBetweenClustersFunction implements Function<ClusteringContext> {

	@Override
	public void apply(ClusteringContext context) {

		if (context.getClusters().size() > 2) {

			int r = ClusteringRandom.getInstance().nextInt(0, context.getClusters().size() - 1);

			Cluster cluster1 = context.getClusters().get(r);
			Cluster cluster2 = context.getClusters().get(0);

			if (!cluster1.getPoints().isEmpty()) {

				double minDist = Double.MAX_VALUE;
				for (Cluster c : context.getClusters()) {
					if (c != cluster1) {
						double dist = context.getDistanceFunction()
								.apply(Lists.newArrayList(cluster1.getCentroid(), c.getCentroid()));
						if (dist < minDist) {
							minDist = dist;
							cluster2 = c;
						}
					}
				}

				Point minPoint = cluster1.getPoints().get(0);
				minDist = Double.MAX_VALUE;
				for (Point p : cluster1.getPoints()) {
					double dist = context.getDistanceFunction().apply(Lists.newArrayList(p, cluster2.getCentroid()));
					if (dist < minDist) {
						minDist = dist;
						minPoint = p;
					}
				}

				if (minPoint != null && cluster2 != null) {
					cluster1.getPoints().remove(minPoint);

					if (!cluster1.isEmpty())
						cluster1.updateCentroid();
					else
						context.getClusters().remove(cluster1);

					cluster2.addPoint(minPoint);
					cluster2.updateCentroid();
				}
			}

		}

	}

	@Override
	public String toString() {
		return "MoveBetweenClustersFunction";
	}

}
