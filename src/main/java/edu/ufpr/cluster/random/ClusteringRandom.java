package edu.ufpr.cluster.random;

import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;
import org.uma.jmetal.util.pseudorandom.impl.JavaRandomGenerator;

/**
 *
 *
 * @author Vidal
 */
public class ClusteringRandom {
	
	private static final String ERROR_MSG = "Please make sure to call getNewInstance for this random class"; 

	private static ClusteringRandom instance;
	private PseudoRandomGenerator randomGenerator;

	private ClusteringRandom() {
		randomGenerator = new JavaRandomGenerator();
	}

	public static ClusteringRandom getNewInstance() {
		return instance = new ClusteringRandom();

	}

	public static ClusteringRandom getInstance() {
		if (instance == null) {
			throw new RuntimeException(ERROR_MSG);
		}
		return instance;
	}

	public void setRandomGenerator(PseudoRandomGenerator randomGenerator) {
		this.randomGenerator = randomGenerator;
	}

	public int nextInt(int lowerBound, int upperBound) {
		return randomGenerator.nextInt(lowerBound, upperBound);
	}

	public double nextDouble() {
		return randomGenerator.nextDouble();
	}

	public double nextDouble(double lowerBound, double upperBound) {
		return randomGenerator.nextDouble(lowerBound, upperBound);
	}

	public void setSeed(long seed) {
		randomGenerator.setSeed(seed);
	}

	public long getSeed() {
		return randomGenerator.getSeed();
	}

	public String getGeneratorName() {
		return randomGenerator.getName();
	}
}
