package edu.ufpr.cluster.algorithms.functions;

import edu.ufpr.cluster.algorithm.ClusteringContext;

public abstract class InitializiationFunction implements Function<ClusteringContext> {

	public InitializiationFunction(int initialK) {
        this.initialK = initialK;
	}

	private int initialK;

	public int getInitialK() {
		return initialK;
	}

	public void setInitialK(int initialK) {
		this.initialK = initialK;
	}

}
