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
public class DuplicationMutation implements MutationOperator<VariableIntegerSolution> {

    private double mutationProbability;
    private JMetalRandom randomGenerator;

    /**
     * Constructor
     */
    public DuplicationMutation(double duplicationProbability) {
        if (duplicationProbability < 0) {
            throw new JMetalException("Mutation probability is negative: " + duplicationProbability);
        }
        this.mutationProbability = duplicationProbability;
        randomGenerator = JMetalRandom.getInstance();
    }

    /* Getter */
    public double getDuplicationProbability() {
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

        doDuplication(mutationProbability, solution);
        return solution;
    }

    /**
     * Perform the mutation operation
     *
     * @param probability Mutation setProbability
     * @param solution The solution to mutate
     */
    public void doDuplication(double probability, VariableIntegerSolution solution) {
        if (randomGenerator.nextDouble() <= probability) {
            Integer firstIndex = randomGenerator.nextInt(0, solution.getNumberOfVariables() - 1);
            Integer lastIndex = randomGenerator.nextInt(0, solution.getNumberOfVariables() - 1);
            if (firstIndex > lastIndex) {
                int aux = firstIndex;
                firstIndex = lastIndex;
                lastIndex = aux;
            }

            for (int i = firstIndex; i <= lastIndex; i++) {
                solution.addVariable(solution.getVariableValue(i));
            }
        }
    }
}