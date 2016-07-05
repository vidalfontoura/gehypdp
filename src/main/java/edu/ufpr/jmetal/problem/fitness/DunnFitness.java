package edu.ufpr.jmetal.problem.fitness;

import java.util.List;

import com.google.common.collect.Lists;

import edu.ufpr.cluster.algorithm.Cluster;
import edu.ufpr.cluster.algorithm.ClusteringContext;
import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.cluster.algorithms.functions.DistanceFunction;
import edu.ufpr.cluster.algorithms.functions.impl.EucledianDistanceFunction;

public class DunnFitness implements FitnessFunction {

	DistanceFunction distanceFunction = null;
	
	@Override
	public Double apply(ClusteringContext context) {
		
		distanceFunction = new EucledianDistanceFunction();
		
		return minDistBetweenClusters(context.getClusters()) / maxClustersDiameter(context.getClusters());
	}

	public double minDistBetweenClusters(List<Cluster> clusters) {
		
		double minInterClusterDistance = Double.MAX_VALUE;
		
		for(Cluster c1: clusters) {
			for(Cluster c2: clusters) {
				if(c1 != c2) {
					double dist = distanceFunction.apply(Lists.newArrayList(c1.getCentroid(), c2.getCentroid()));
					if(dist < minInterClusterDistance) {
						minInterClusterDistance = dist;
					}
				}
			}
		}
		
		return minInterClusterDistance;
	}
	
	public double maxClustersDiameter(List<Cluster> clusters) {
		double maxClusterDiameter = 0.0;
		
		List<Point> points;
		
		for(Cluster c: clusters) {
			points = c.getPoints();
			for(Point p1: points) {
				for(Point p2: points) {
					if(p1 != p2) {
						double dist = distanceFunction.apply(Lists.newArrayList(p1, p2));
						
						if(dist > maxClusterDiameter) {
							maxClusterDiameter = dist;
						}
					}
				}
			}
		}
		
		return maxClusterDiameter;
	}
}
