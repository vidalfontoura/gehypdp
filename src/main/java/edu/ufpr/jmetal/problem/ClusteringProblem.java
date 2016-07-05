package edu.ufpr.jmetal.problem;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import edu.ufpr.cluster.algorithm.ClusteringAlgorithm;
import edu.ufpr.cluster.algorithm.ClusteringContext;
import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.cluster.random.ClusteringRandom;
import edu.ufpr.ge.mapper.impl.ClusteringExpressionGrammarMapper;
import edu.ufpr.jmetal.problem.fitness.FitnessFunction;
import edu.ufpr.jmetal.solution.impl.VariableIntegerSolution;

public class ClusteringProblem extends AbstractGrammaticalEvolutionProblem {

    private static final long serialVersionUID = 1L;

    private int minCondons;
    private int maxCondons;

    private FitnessFunction fitnessFunction;

    private List<Point> points;
    private int numberOfClusteringSeeds;

    private int evaluationCount = 0;

    private int generationsCount = 0;

    private double bestFitnessPerGen = -1.1;

    private double bestFitness = -1.1;

    private String bestIndividual = "";

    private double bestPerGenK = -1;

    private double bestK = -1;

    private int populationSize;

    private int maxClusteringExecutions;

    public ClusteringProblem(String grammarFile, List<Point> points, int minCondons, int maxCondons,
        int numberOfClusteringSeeds, FitnessFunction fitnessFunction, int populationSize, int maxClusteringExecutions,
        int maxAlgorithmDepth) {

        super(new ClusteringExpressionGrammarMapper(maxAlgorithmDepth), grammarFile);
        this.maxCondons = maxCondons;
        this.minCondons = minCondons;
        this.points = points;
        this.fitnessFunction = fitnessFunction;
        this.numberOfClusteringSeeds = numberOfClusteringSeeds;
        this.populationSize = populationSize;
        this.maxClusteringExecutions = maxClusteringExecutions;

    }

    @Override
    public int getNumberOfVariables() {

        return 0;
    }

    @Override
    public int getNumberOfObjectives() {

        return 1;
    }

    @Override
    public int getNumberOfConstraints() {

        return 0;
    }

    @Override
    public String getName() {

        return "Clustering GE Integer Problem";
    }

    public void buildClusteringAlgorithm() {

    }

    @Override
    public void evaluate(VariableIntegerSolution solution) {

        int numberOfVariables = solution.getNumberOfVariables();

        List<Integer> clusteringSolution = new ArrayList<>();
        StringBuilder solutionStr = new StringBuilder();
        for (int i = 0; i < numberOfVariables; i++) {

            solutionStr.append(solution.getVariableValue(i)).append(",");
            clusteringSolution.add(solution.getVariableValue(i));
        }
        solutionStr.deleteCharAt(solutionStr.length() - 1);

        List<Double> fitnesses = Lists.newArrayList();
        List<Integer> clustersAmount = Lists.newArrayList();

        for (int i = 0; i < numberOfClusteringSeeds; i++) {
            ClusteringRandom.getNewInstance().setSeed(i);

            ClusteringAlgorithm clusteringAlgorithm = (ClusteringAlgorithm) mapper.interpret(clusteringSolution);

            // Clean up points before the execution
            points.stream().forEach(p -> {
                p.clearCluster();
            });

            clusteringAlgorithm.setPoints(points);
            clusteringAlgorithm.setMaxEvaluations(maxClusteringExecutions);

            ClusteringContext clusteringContext = clusteringAlgorithm.execute();

            Double fitness = this.fitnessFunction.apply(clusteringContext);

            fitnesses.add(fitness);

            clustersAmount.add(clusteringContext.getClusters().size());

        }

        double sumFitnesses = fitnesses.stream().mapToDouble(Double::doubleValue).sum();

        double avgfitness = sumFitnesses / fitnesses.size();

        double sumKs = clustersAmount.stream().mapToInt(Integer::intValue).sum();

        double avgK = sumKs / clustersAmount.size();

        if (avgfitness > bestFitnessPerGen) {
            bestFitnessPerGen = avgfitness;
            bestIndividual = solutionStr.toString();
            bestPerGenK = avgK;
        }

        if (avgfitness > bestFitness) {
            bestFitness = avgfitness;
            bestK = avgK;
        }

        if (evaluationCount % populationSize == 0) {
            System.out.print("Generation: " + generationsCount + "; ");
            System.out.println(bestFitnessPerGen + "; k: " + bestPerGenK + "; " + bestIndividual);
            // System.out.println("Individual: " + bestIndividual);
            System.out.println("Best fitness found so far: " + bestFitness + "; k: " + bestK);

            bestIndividual = "";
            bestFitnessPerGen = -1.1;
            bestPerGenK = -1;
            generationsCount++;
        }
        solution.setObjective(0, avgfitness * -1);
        evaluationCount++;

    }

    @Override
    public VariableIntegerSolution createSolution() {

        return new VariableIntegerSolution(this, minCondons, maxCondons);
    }

}
