package edu.ufpr.jmetal.algorithm.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
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
import edu.ufpr.jmetal.problem.fitness.SilhouetteFitness;
import edu.ufpr.jmetal.problem.old.impl.DataInstanceReader;
import edu.ufpr.jmetal.solution.impl.VariableIntegerSolution;
import edu.ufpr.math.utils.MathUtils;

public class ClusteringExperiment extends AbstractAlgorithmRunner {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        String outputDirName = "result-30-iris";
        File outDir = new File(outputDirName);
        if (!outDir.exists()) {
            outDir.mkdir();
        }
        String grammarFile = "/clustergrammar.bnf";
        String databaseFile = "/iris.data";
        double crossoverProbability = 0.95;
        double mutationProbability = 0.1;
        double pruneMutationProbability = 0.05;
        double duplicationProbability = 0.05;
        int pruneIndex = 10;
        int maxEvaluations = 20000;
        int populationSize = 100;
        int clusteringExecutionSeed = 100;
        String dataType = "Double";
        boolean classIncluded = true;
        int maxClusteringEvaluations = 1000;
        int maxAlgorithmDepth = 20;

        FitnessFunction clusteringFitnessFunction = new SilhouetteFitness();
        List<Point> points =
            MathUtils.normalizeData(DataInstanceReader.readPoints(databaseFile, dataType, classIncluded));

        for (int i = 0; i < 10; i++) {

            JMetalRandom.getInstance().setSeed(i);

            String outputFolder = outDir + File.separator + i;
            File outputFolderFile = new File(outputFolder);
            outputFolderFile.mkdir();
            File executionLog = new File(outputFolder + File.separator + "execution.log");

            FileOutputStream fos = new FileOutputStream(executionLog);
            PrintStream ps = new PrintStream(fos);
            System.setOut(ps);

            System.out.println("Seed: " + i);

            ClusteringProblem problem = new ClusteringProblem(grammarFile, points, 1, 20, clusteringExecutionSeed,
                clusteringFitnessFunction, populationSize, maxClusteringEvaluations, maxAlgorithmDepth);

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

            System.out.println("Total time of execution of seed " + i + " : " + computingTime);
            System.out.println("Solution Fitness: " + population.get(0).getObjective(0));
            System.out.println("Variables: " + mapper.interpret(population.get(0)));

            printFinalSolutionSet(population, outputFolder);

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

    static public void printObjectivesToFile(FileOutputContext context, List<? extends Solution<?>> solutionSet) {

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
