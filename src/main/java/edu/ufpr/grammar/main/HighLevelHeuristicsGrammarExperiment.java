/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ufpr.grammar.main;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import edu.ufpr.grammar.algorithm.GrammaticalEvolutionAlgorithm;
import edu.ufpr.grammar.mapper.SymbolicExpressionGrammarMapper;
import edu.ufpr.grammar.problem.HighLevelHeuristicGrammarProblem;
import edu.ufpr.grammar.solution.VariableIntegerSolution;
import edu.ufpr.jmetal.operator.crossover.SinglePointCrossoverVariableLength;
import edu.ufpr.jmetal.operator.mutation.DuplicationMutation;
import edu.ufpr.jmetal.operator.mutation.IntegerMutation;
import edu.ufpr.jmetal.operator.mutation.PruneMutation;

/**
 *
 * @author thaina
 */
public class HighLevelHeuristicsGrammarExperiment {

	public static void main(String[] args) {

		//
		// if (args.length < 30) {
		// System.err.println("Usage: java -jar
		// ge-clustering-jar-with-dependencies.jar "
		// + "-g [grammar file] -d [database] -dt [data type] -m [max
		// evaluations] -r [result folder]"
		// + " -s [seed] -p [populationSize] -minC [minCondons] -maxC
		// [maxCondons] -t [threads pool size]"
		// + "-cx [crossover probabilty] -mx [mutation probability] -pi [prune
		// index] -px [prune probability] "
		// + "-dx [duplication probability] -cs [number of clustering seeds] -dt
		// [dataType] -mc [max clustering executions] \n");
		// System.exit(1);
		// }
		//
		int timelimit = 180000;
		int maxEvaluations = 1000;
		int populationSize = 10;
		int memorySize = 12;

		int numberOfInsideSeeds = 1;

		int rcWindowSize = 10;
		int[] instances = new int[] { 5, 7, 11 };
		if (args.length == 3) {
			long seed = Long.valueOf(args[0]);
			JMetalRandom.getInstance().setSeed(seed);
			timelimit = Integer.valueOf(args[1]);
			maxEvaluations = Integer.valueOf(args[2]);
			populationSize = Integer.valueOf(args[3]);
		} else {
			JMetalRandom.getInstance().setSeed(1l);
		}

		HighLevelHeuristicGrammarProblem problem = new HighLevelHeuristicGrammarProblem(5, 20, "/selectiongrammar.bnf",
				"/acceptancegrammar.bnf", numberOfInsideSeeds, memorySize, timelimit, rcWindowSize, instances);

		CrossoverOperator crossover = new SinglePointCrossoverVariableLength(1);
		MutationOperator mutation = new IntegerMutation(0.1);
		SelectionOperator selection = new BinaryTournamentSelection();
		PruneMutation pruneMutation = new PruneMutation(0.01, 5);
		DuplicationMutation duplicationMutation = new DuplicationMutation(0.01);

		GrammaticalEvolutionAlgorithm algorithm = new GrammaticalEvolutionAlgorithm(problem, maxEvaluations,
				populationSize, crossover, mutation, selection, pruneMutation, duplicationMutation,
				new SequentialSolutionListEvaluator());

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
		VariableIntegerSolution solution = algorithm.getResult();
		long computingTime = algorithmRunner.getComputingTime();

		SymbolicExpressionGrammarMapper mapperSelection = new SymbolicExpressionGrammarMapper();
		mapperSelection.loadGrammar("/selectiongrammar.bnf");

		SymbolicExpressionGrammarMapper mapperAcceptance = new SymbolicExpressionGrammarMapper();
		mapperAcceptance.loadGrammar("/acceptancegrammar.bnf");

		System.out.println("Total time of execution: " + computingTime);
		System.out.println("Solution: " + solution.getObjective(0));
		System.out.println("Variables selection: " + mapperSelection.interpret(solution));
		System.out.println("Variables acceptance: " + mapperAcceptance.interpret(solution));
	}
}
