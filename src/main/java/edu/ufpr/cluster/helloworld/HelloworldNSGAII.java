package edu.ufpr.cluster.helloworld;

import java.io.FileNotFoundException;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.ProblemUtils;

public class HelloworldNSGAII  extends AbstractAlgorithmRunner {

	
	public static void main(String[] args) throws FileNotFoundException {
		Problem<IntegerSolution> problem;
	    Algorithm<List<IntegerSolution>> algorithm;
	    CrossoverOperator<IntegerSolution> crossover;
	    MutationOperator<IntegerSolution> mutation;
	    SelectionOperator<List<IntegerSolution>, IntegerSolution> selection;
		    
	    String problemName ;
	    
	    problemName = "org.uma.jmetal.problem.multiobjective.NMMin" ;
	    String referenceParetoFront = "";
	    
	    problem = ProblemUtils.<IntegerSolution> loadProblem(problemName);

	    
	    double crossoverProbability = 0.9 ;
	    double crossoverDistributionIndex = 20.0 ;
	    crossover = new IntegerSBXCrossover(crossoverProbability, crossoverDistributionIndex) ;

	    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
	    double mutationDistributionIndex = 20.0 ;
	    mutation = new IntegerPolynomialMutation(mutationProbability, mutationDistributionIndex) ;

	    selection = new BinaryTournamentSelection<IntegerSolution>() ;
		
		algorithm = new NSGAIIBuilder<IntegerSolution>(problem, crossover, mutation)
	            .setSelectionOperator(selection)
	            .setMaxIterations(250)
	            .setPopulationSize(100)
	            .build() ;
		
		 AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
		            .execute() ;
		 
		 List<IntegerSolution> population = algorithm.getResult() ;
		    long computingTime = algorithmRunner.getComputingTime() ;

		    
	    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
	    printFinalSolutionSet(population);
	    if (!referenceParetoFront.equals("")) {
	        printQualityIndicators(population, referenceParetoFront) ;
	      }
	}
}
