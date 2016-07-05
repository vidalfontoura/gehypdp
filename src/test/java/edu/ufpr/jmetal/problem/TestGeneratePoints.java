/*
 * Copyright 2015, Charter Communications,  All rights reserved.
 */
package edu.ufpr.jmetal.problem;

import edu.ufpr.cluster.random.ClusteringRandom;

/**
 *
 *
 * @author Vidal
 */
public class TestGeneratePoints {

	public static void main(String[] args) {
		ClusteringRandom.getNewInstance().setSeed(100);

		for (int i = 0; i < 1000; i++) {
			double x = ClusteringRandom.getInstance().nextDouble(0, 1);
			double y = ClusteringRandom.getInstance().nextDouble(0, 1);
			System.out.println(x + "," + y);
		}
	}
}
