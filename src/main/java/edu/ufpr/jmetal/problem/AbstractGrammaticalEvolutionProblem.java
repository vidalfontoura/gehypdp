package edu.ufpr.jmetal.problem;


import org.uma.jmetal.problem.impl.AbstractGenericProblem;

import edu.ufpr.ge.mapper.AbstractGrammarMapper;
import edu.ufpr.jmetal.solution.impl.VariableIntegerSolution;

public abstract class AbstractGrammaticalEvolutionProblem extends AbstractGenericProblem<VariableIntegerSolution> {

    protected AbstractGrammarMapper mapper;

    public AbstractGrammaticalEvolutionProblem(AbstractGrammarMapper mapper, String file) {
        this.mapper = mapper;
        mapper.loadGrammar(file);
    }

    public Integer getLowerBound(int i) {
        return 1;
    }

    public Integer getUpperBound(int i) {
        return 250;
    }

}
