/*
 * Copyright 2016, Charter Communications,  All rights reserved.
 */
package edu.ufpr.grammar.problem;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import HyPDP.CustomHH;
import PDP.PDP;
import edu.ufpr.grammar.mapper.AbstractGrammarMapper;
import edu.ufpr.grammar.mapper.SymbolicExpressionGrammarMapper;
import edu.ufpr.grammar.solution.VariableIntegerSolution;

/**
 *
 *
 * @author vfontoura
 */
public class HighLevelHeuristicGrammarProblem extends AbstractGrammaticalEvolutionProblem<String> {

	protected AbstractGrammarMapper<String> mapperSelection;
	protected AbstractGrammarMapper<String> mapperAcceptance;

	protected int numberOfSeeds;
	protected int memorySize;
	protected int timeLimit;
	protected int rcWindowSize;
	protected int[] instances;

	private Map<Integer, InstanceFitnessBoundaries> instanceBoundaries;

	public HighLevelHeuristicGrammarProblem(int minCondons, int maxCondons, String selectionGrammarFile,
			String acceptanceGrammarFile, int numberOfSeeds, int memorySize, int timeLimit, int rcWindowSize,
			int[] instances) {

		super(minCondons, maxCondons);
		mapperSelection = new SymbolicExpressionGrammarMapper();
		mapperSelection.loadGrammar(selectionGrammarFile);

		mapperAcceptance = new SymbolicExpressionGrammarMapper();
		mapperAcceptance.loadGrammar(acceptanceGrammarFile);

		this.instances = instances;
		this.numberOfSeeds = numberOfSeeds;
		this.timeLimit = timeLimit;
		this.rcWindowSize = rcWindowSize;
		this.memorySize = memorySize;

		setNumberOfObjectives(1);

		instanceBoundaries = Maps.newHashMap();
		instanceBoundaries.put(1, new InstanceFitnessBoundaries(0, 9));
		instanceBoundaries.put(2, new InstanceFitnessBoundaries(0, 9));
		instanceBoundaries.put(3, new InstanceFitnessBoundaries(0, 8));
		instanceBoundaries.put(4, new InstanceFitnessBoundaries(0, 14));
		instanceBoundaries.put(5, new InstanceFitnessBoundaries(0, 23));
		instanceBoundaries.put(6, new InstanceFitnessBoundaries(0, 21));
		instanceBoundaries.put(7, new InstanceFitnessBoundaries(0, 36));
		instanceBoundaries.put(8, new InstanceFitnessBoundaries(0, 42));
		instanceBoundaries.put(9, new InstanceFitnessBoundaries(0, 53));
		instanceBoundaries.put(10, new InstanceFitnessBoundaries(0, 48));
		instanceBoundaries.put(11, new InstanceFitnessBoundaries(0, 50));

	}

	public void evaluate(VariableIntegerSolution solution) {

		String selectionFunction = mapperSelection.interpret(solution);
		String acceptanceFunction = mapperAcceptance.interpret(solution);

		System.out.println("Selection function: " + selectionFunction);
		System.out.println("Acceptance function: " + acceptanceFunction);

		List<Double> fitnesses = Lists.newArrayList();
		for (int j = 0; j < instances.length; j++) {
			int instance = instances[j];

			for (int i = 0; i < numberOfSeeds; i++) {
				CustomHH customHH = new CustomHH(i, memorySize, selectionFunction, acceptanceFunction, rcWindowSize,
						instance);

				PDP problem = new PDP(i);
				problem.loadInstance(instance);

				customHH.setTimeLimit(timeLimit);
				customHH.loadProblemDomain(problem);
				customHH.run();

				double fitness = customHH.getBestFitness();

				System.out.println(
						"End of execution of EG Instance: " + instance + " Seed: " + i + " Fitness: " + fitness);

				double min = instanceBoundaries.get(instance).getMin();

				double normalized = Math.abs(fitness) / min;

				System.out.println("Normalized fitness: " + normalized);

				fitnesses.add(normalized);
			}

		}
		double sum = fitnesses.stream().mapToDouble(Double::doubleValue).sum();
		System.out.println("Overall fitness: " + sum);
		solution.setObjective(0, sum * -1);
	}

	class InstanceFitnessBoundaries {
		private double max;
		private double min;

		public InstanceFitnessBoundaries(double max, double min) {
			this.max = max;
			this.min = min;
		}

		public double getMax() {
			return max;
		}

		public void setMax(double max) {
			this.max = max;
		}

		public double getMin() {
			return min;
		}

		public void setMin(double min) {
			this.min = min;
		}

	}

}
