package edu.ufpr.ge.operators.mutation;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import edu.ufpr.jmetal.solution.impl.VariableIntegerSolution;

/**
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * This class implements a bit flip mutation operator.
 */
public class IntegerMutation implements MutationOperator<VariableIntegerSolution> {

    private double mutationProbability;
    private JMetalRandom randomGenerator;

    /**
     * Constructor
     */
    public IntegerMutation(double mutationProbability) {
        if (mutationProbability < 0) {
            throw new JMetalException("Mutation probability is negative: " + mutationProbability);
        }
        this.mutationProbability = mutationProbability;
        randomGenerator = JMetalRandom.getInstance();
    }

    /* Getter */
    public double getMutationProbability() {
        return mutationProbability;
    }

    /**
     * Execute() method
     */
    @Override
    public VariableIntegerSolution execute(VariableIntegerSolution solution) {
        if (null == solution) {
            throw new JMetalException("Null parameter");
        }

        doMutation(mutationProbability, solution);
        return solution;
    }

    /**
     * Perform the mutation operation
     *
     * @param probability Mutation setProbability
     * @param solution The solution to mutate
     */
    public void doMutation(double probability, VariableIntegerSolution solution) {
        final Integer lowerBound = solution.getLowerBound(0);
        final Integer upperBound = solution.getUpperBound(0);
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            if (randomGenerator.nextDouble() <= probability) {
                solution.setVariableValue(i, randomGenerator.nextInt(lowerBound, upperBound));
            }
        }
    }
}