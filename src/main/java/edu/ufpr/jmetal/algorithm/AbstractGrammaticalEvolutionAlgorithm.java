package edu.ufpr.jmetal.algorithm;

import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;

import edu.ufpr.ge.operators.mutation.DuplicationMutation;
import edu.ufpr.ge.operators.mutation.PruneMutation;
import edu.ufpr.jmetal.solution.impl.VariableIntegerSolution;

/**
 * Created by ajnebro on 26/10/14.
 */
public abstract class AbstractGrammaticalEvolutionAlgorithm<S extends VariableIntegerSolution, R>
		extends AbstractGeneticAlgorithm<VariableIntegerSolution, R> {

	protected PruneMutation pruneMutationOperator;
	protected DuplicationMutation duplicationMutationOperator;

}