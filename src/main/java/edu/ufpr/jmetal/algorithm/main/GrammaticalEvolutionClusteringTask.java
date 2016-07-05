package edu.ufpr.jmetal.algorithm.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.fileoutput.FileOutputContext;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.ge.mapper.impl.SymbolicExpressionGrammarMapper;
import edu.ufpr.ge.operators.crossover.SinglePointCrossoverVariableLength;
import edu.ufpr.ge.operators.mutation.DuplicationMutation;
import edu.ufpr.ge.operators.mutation.IntegerMutation;
import edu.ufpr.ge.operators.mutation.PruneMutation;
import edu.ufpr.jmetal.algorithm.impl.GrammaticalEvolutionAlgorithm;
import edu.ufpr.jmetal.problem.ClusteringProblem;
import edu.ufpr.jmetal.problem.fitness.FitnessFunction;
import edu.ufpr.jmetal.solution.impl.VariableIntegerSolution;

/**
 *
 *
 * @author Vidal
 */
public class GrammaticalEvolutionClusteringTask implements Runnable {

    private String resultDirName = "";
    private String grammarFile = "";
    private double crossoverProbability;
    private double mutationProbability;
    private double pruneMutationProbability;
    private double duplicationProbability;
    private int pruneIndex;
    private int maxEvaluations;
    private int populationSize;
    private int numberOfClusteringExecutionsSeed;
    private int seed;
    private int minCondons;
    private int maxCondons;
    private FitnessFunction fitnessFunction;
    private List<Point> points;
    private int maxClusteringEvaluations;
    private int maxAlgorithmDepth;

    public GrammaticalEvolutionClusteringTask(String resultDirName, String grammarFile, double crossoverProbability,
        double mutationProbability, double pruneMutationProbability, double duplicationProbability, int pruneIndex,
        int maxEvaluations, int populationSize, int numberOfClusteringExecutionSeed, int seed, int minCondons,
        int maxCondons, FitnessFunction fitnessFunction, List<Point> points, int maxClusteringEvaluations,
        int maxAlgorithmDepth) {
        this.resultDirName = resultDirName;
        this.grammarFile = grammarFile;
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability = mutationProbability;
        this.pruneMutationProbability = pruneMutationProbability;
        this.duplicationProbability = duplicationProbability;
        this.pruneIndex = pruneIndex;
        this.maxEvaluations = maxEvaluations;
        this.populationSize = populationSize;
        this.numberOfClusteringExecutionsSeed = numberOfClusteringExecutionSeed;
        this.seed = seed;
        this.minCondons = minCondons;
        this.maxCondons = maxCondons;
        this.fitnessFunction = fitnessFunction;
        this.points = points;
        this.maxClusteringEvaluations = maxClusteringEvaluations;
        this.maxAlgorithmDepth = maxAlgorithmDepth;
    }

    public void run() {

        try {
            JMetalRandom.getInstance().setSeed(seed);

            String outputFolder = resultDirName + File.separator + seed;
            File outputFolderFile = new File(outputFolder);
            if (!outputFolderFile.exists()) {
                outputFolderFile.mkdir();
            }

            File executionLog = new File(outputFolder + File.separator + "execution.log");

            FileOutputStream fos = new FileOutputStream(executionLog);
            PrintStream ps = new PrintStream(fos);

            System.setOut(ps);
            ClusteringProblem problem =
                new ClusteringProblem(grammarFile, points, minCondons, maxCondons, numberOfClusteringExecutionsSeed,
                    fitnessFunction, populationSize, maxClusteringEvaluations, maxAlgorithmDepth);

            CrossoverOperator<VariableIntegerSolution> crossoverOperator =
                new SinglePointCrossoverVariableLength(crossoverProbability);
            MutationOperator<VariableIntegerSolution> mutationOperator = new IntegerMutation(mutationProbability);
            SelectionOperator selectionOperator = new BinaryTournamentSelection<>();

            PruneMutation pruneMutationOperator = new PruneMutation(pruneMutationProbability, pruneIndex);
            DuplicationMutation duplicationMutationOperator = new DuplicationMutation(duplicationProbability);

            GrammaticalEvolutionAlgorithm algorithm = new GrammaticalEvolutionAlgorithm(problem, maxEvaluations,
                populationSize, crossoverOperator, mutationOperator, selectionOperator, pruneMutationOperator,
                duplicationMutationOperator, new SequentialSolutionListEvaluator());

            AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
            List<VariableIntegerSolution> population = algorithm.getResult();
            long computingTime = algorithmRunner.getComputingTime();

            SymbolicExpressionGrammarMapper mapper = new SymbolicExpressionGrammarMapper();
            mapper.loadGrammar(grammarFile);

            System.out.println("Total time of execution of seed " + seed + " : " + computingTime);
            System.out.println("Solution Fitness: " + population.get(0).getObjective(0));
            System.out.println("Variables: " + mapper.interpret(population.get(0)));

            printFinalSolutionSet(population, outputFolder);

        } catch (Exception e) {
            System.out.println("Error occurred");
        }

    }

    public static Builder builder() {

        return new Builder();
    }

    public static final class Builder {

        private String resultDirName = "";
        private String grammarFile = "";
        private double crossoverProbability;
        private double mutationProbability;
        private double pruneMutationProbability;
        private double duplicationProbability;
        private int pruneIndex;
        private int maxEvaluations;
        private int populationSize;
        private int clusteringExecutionSeed;
        private int seed;
        private int minCondons;
        private int maxCondons;
        private FitnessFunction fitnessFunction;
        private List<Point> points;

        private int maxClusteringEvaluations;
        private int maxAlgorithmDepth;

        public Builder withResultDirName(String resultDirName) {

            this.resultDirName = resultDirName;
            return this;
        }

        public Builder withGrammarFile(String grammarFile) {

            this.grammarFile = grammarFile;
            return this;
        }

        public Builder withCrossoverProbability(double crossoverProbability) {

            this.crossoverProbability = crossoverProbability;
            return this;
        }

        public Builder withMutationProbability(double mutationProbability) {

            this.mutationProbability = mutationProbability;
            return this;
        }

        public Builder withPruneMutationProbability(double pruneMutationProbability) {

            this.pruneMutationProbability = pruneMutationProbability;
            return this;
        }

        public Builder withDuplicationProbability(double duplicationProbability) {

            this.duplicationProbability = duplicationProbability;
            return this;
        }

        public Builder withPruneIndex(int pruneIndex) {

            this.pruneIndex = pruneIndex;
            return this;
        }

        public Builder withMaxEvaluations(int maxEvaluations) {

            this.maxEvaluations = maxEvaluations;
            return this;
        }

        public Builder withPopulationSize(int populationSize) {

            this.populationSize = populationSize;
            return this;
        }

        public Builder withClusteringExecutionSeed(int clusteringExecutionSeed) {

            this.clusteringExecutionSeed = clusteringExecutionSeed;
            return this;
        }

        public Builder withSeed(int seed) {

            this.seed = seed;
            return this;
        }

        public Builder withMinCondons(int minCondons) {

            this.minCondons = minCondons;
            return this;
        }

        public Builder withMaxCondons(int maxCondons) {

            this.maxCondons = maxCondons;
            return this;
        }

        public Builder withFitnessFunction(FitnessFunction fitnessFunction) {

            this.fitnessFunction = fitnessFunction;
            return this;
        }

        public Builder withPoints(List<Point> points) {

            this.points = points;
            return this;
        }

        public Builder withMaxClusteringExecutions(int maxClusteringExecutions) {

            this.maxClusteringEvaluations = maxClusteringExecutions;
            return this;
        }

        public Builder withMaxAlgorithmDepth(int maxAlgorithmDepth) {

            this.maxAlgorithmDepth = maxAlgorithmDepth;
            return this;
        }

        public GrammaticalEvolutionClusteringTask build() {

            return new GrammaticalEvolutionClusteringTask(resultDirName, grammarFile, crossoverProbability,
                mutationProbability, pruneMutationProbability, duplicationProbability, pruneIndex, maxEvaluations,
                populationSize, clusteringExecutionSeed, seed, minCondons, maxCondons, fitnessFunction, points,
                maxClusteringEvaluations, maxAlgorithmDepth);
        }

    }

    static public void printVariablesToFile(FileOutputContext context, List<? extends Solution<?>> solutionSet) {

        BufferedWriter bufferedWriter = context.getFileWriter();

        try {
            for (int i = 0; i < solutionSet.size(); i++) {
                for (int j = 0; j < solutionSet.get(i).getNumberOfVariables(); j++) {
                    bufferedWriter.write(solutionSet.get(i).getVariableValueString(j) + context.getSeparator());
                }
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            throw new JMetalException("Exception when printing variables to file", e);
        }
    }

    public static void printObjectivesToFile(FileOutputContext context, List<? extends Solution<?>> solutionSet) {

        BufferedWriter bufferedWriter = context.getFileWriter();

        int numberOfObjectives = solutionSet.get(0).getNumberOfObjectives();
        try {
            for (int i = 0; i < solutionSet.size(); i++) {
                for (int j = 0; j < numberOfObjectives; j++) {
                    bufferedWriter.write(solutionSet.get(i).getObjective(j) + context.getSeparator());
                }
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            throw new JMetalException("Exception when printing objectives to file", e);
        }
    }

    public static void printFinalSolutionSet(List<? extends Solution<?>> population, String outputFolder) {

        printVariablesToFile(new DefaultFileOutputContext(outputFolder + File.separator + "VAR.csv"), population);
        printObjectivesToFile(new DefaultFileOutputContext(outputFolder + File.separator + "FUN.csv"), population);

        JMetalLogger.logger.info("Random seed: " + JMetalRandom.getInstance().getSeed());
        JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
        JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");
    }

}
