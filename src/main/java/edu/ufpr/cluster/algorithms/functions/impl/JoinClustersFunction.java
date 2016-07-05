package edu.ufpr.cluster.algorithms.functions.impl;

import com.google.common.collect.Lists;

import edu.ufpr.cluster.algorithm.Cluster;
import edu.ufpr.cluster.algorithm.ClusteringContext;
import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.cluster.algorithms.functions.Function;
import edu.ufpr.cluster.random.ClusteringRandom;

public class JoinClustersFunction implements Function<ClusteringContext> {

	@Override
	public void apply(ClusteringContext context) {

		if (context.getClusters().size() > 2) {

			int r = ClusteringRandom.getInstance().nextInt(0, context.getClusters().size() - 1);

			Cluster cluster1 = context.getClusters().get(r);
			Cluster cluster2 = context.getClusters().get(0);

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

			for(Point p : cluster2.getPoints()) {
				cluster1.addPoint(p);
			}
			cluster1.updateCentroid();
			cluster2.getPoints().clear();
			context.getClusters().remove(cluster2);
		}

	}

	@Override
	public String toString() {
		return "JoinClustersFunction";
	}

}
