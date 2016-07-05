package edu.ufpr.cluster.algorithms.functions.impl;

import edu.ufpr.cluster.algorithm.Cluster;
import edu.ufpr.cluster.algorithm.ClusteringContext;
import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.cluster.algorithms.functions.InitializiationFunction;

public class UniformCentroidInitilization extends InitializiationFunction {

	public UniformCentroidInitilization(int initialK) {
		super(initialK);
	}

	@Override
	public void apply(ClusteringContext context) {

		if (context.getClusters().size() > 0) {
			throw new RuntimeException("The clusters must not be set already at this point");
		}

		int dimensions = context.getDimensions();

		double[] maxCoordinates = new double[dimensions];
		double[] minCoordinates = new double[dimensions];
		for (int i = 0; i < maxCoordinates.length; i++) {
			maxCoordinates[i] = Double.MIN_VALUE;
		}
		for (int i = 0; i < minCoordinates.length; i++) {
			minCoordinates[i] = Double.MAX_VALUE;
		}
		for (Point point : context.getPoints()) {
			for (int i = 0; i < point.getCoordinates().size(); i++) {
				double coordinate = point.getCoordinates().get(i);
				if (coordinate > maxCoordinates[i]) {
					maxCoordinates[i] = coordinate;
				}
				if (coordinate < minCoordinates[i]) {
					minCoordinates[i] = coordinate;
				}
			}

		}
		double[] increments = new double[dimensions];
		for (int i = 0; i < increments.length; i++) {
			increments[i] = (maxCoordinates[i] - minCoordinates[i]) / getInitialK();
		}
		for (int i = 0; i < getInitialK(); i++) {
			Point point = new Point();

			for (int j = 0; j < dimensions; j++) {
				double coordinate = increments[j] * (i + 1);
				point.getCoordinates().add(coordinate);
			}

			Cluster cluster = new Cluster(point);
			context.getClusters().add(cluster);

		}
	}

	@Override
	public String toString() {
		return "UniformCentroidInitilization";
	}

}
