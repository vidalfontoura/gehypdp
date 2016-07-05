package edu.ufpr.cluster.algorithms.functions.impl;

import java.util.List;

import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.cluster.algorithms.functions.DistanceFunction;

public class ManhattanDistanceFunction extends DistanceFunction {

	@Override
	public Double apply(List<Point> points) {

		// https://lyfat.wordpress.com/2012/05/22/euclidean-vs-chebyshev-vs-manhattan-distance/
		if (points.size() > 2) {
			throw new IllegalArgumentException(
					"Isn't possible to calculate the distance for more than 2 points: " + points.size());
		}

		Point p = points.get(0);
		Point q = points.get(1);

		if (p.getCoordinates().size() != q.getCoordinates().size()) {
			throw new IllegalArgumentException(
					"Isn't possible to calculate the distance the points have different dimensions: p="
							+ p.getCoordinates().size() + ",q=" + q.getCoordinates().size());
		}

		double sum = 0.0;
		for (int i = 0; i < p.getCoordinates().size(); i++) {
			double pCoordinate = p.getCoordinates().get(i);
			double qCoordinate = q.getCoordinates().get(i);

			sum += Math.abs(pCoordinate - qCoordinate);
		}
		return sum;

		// Old code
		// return Math.abs(p.getX() - q.getX()) + Math.abs(p.getY() - q.getY());
	}

	@Override
	public String toString() {
		return "ManhattanDistanceFunction";
	}

}
