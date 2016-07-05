package edu.ufpr.cluster.algorithms.functions.impl;

import edu.ufpr.cluster.algorithm.Cluster;
import edu.ufpr.cluster.algorithm.ClusteringContext;
import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.cluster.algorithms.functions.InitializiationFunction;
import edu.ufpr.cluster.random.ClusteringRandom;

public class RandomCentroidInitilization extends InitializiationFunction {

	public RandomCentroidInitilization(int initialK) {
		super(initialK);
	}

	@Override
	public void apply(ClusteringContext context) {

		if (context.getClusters().size() > 0) {
			throw new RuntimeException("The clusters must not be set already at this point");
		}

		int dimension = context.getDimensions();
		for (int i = 0; i < getInitialK(); i++) {
			//
			Point point = new Point();
			for (int j = 0; j < dimension; j++) {
				// TODO: check how the bound should be configured. I suspect
				// that the bound should be in the range of the problem
				double coordinate = ClusteringRandom.getInstance().nextDouble(2, 10);
				point.getCoordinates().add(coordinate);
			}
			// TODO: May need to check if the points are the same or very near?
			Cluster cluster = new Cluster(point);

			context.getClusters().add(cluster);
		}
	}

	@Override
	public String toString() {
		return "RandomCentroidInitilization";
	}

}
