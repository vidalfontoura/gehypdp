package edu.ufpr.grammar.algorithm;

import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;

import edu.ufpr.grammar.solution.VariableIntegerSolution;
import edu.ufpr.jmetal.operator.mutation.DuplicationMutation;
import edu.ufpr.jmetal.operator.mutation.PruneMutation;

/**
 * Created by ajnebro on 26/10/14.
 */
public abstract class AbstractGrammaticalEvolutionAlgorithm<S extends VariableIntegerSolution, R>
		extends AbstractGeneticAlgorithm<VariableIntegerSolution, R> {

	protected PruneMutation pruneMutationOperator;
	protected DuplicationMutation duplicationMutationOperator;

}
