package edu.ufpr.cluster.algorithms.functions.impl;

import java.util.List;

import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.cluster.algorithms.functions.DistanceFunction;

public class ChebyshevDistanceFunction extends DistanceFunction {

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

		double max = Double.MIN_VALUE;
		for (int i = 0; i < p.getCoordinates().size(); i++) {
			double pCoordinate = p.getCoordinates().get(i);
			double qCoordinate = q.getCoordinates().get(i);

			double diff = Math.abs(pCoordinate - qCoordinate);
			if (diff > max) {
				max = diff;
			}
		}
		return max;
		// Old code two dimension
		// double x = Math.abs(p.getX() - q.getX());
		// double y = Math.abs(p.getY() - q.getY());
		//
		// return x>y?x:y;
	}

	@Override
	public String toString() {
		return "ChebyshevDistanceFunction";
	}

}
