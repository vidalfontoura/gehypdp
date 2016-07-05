package edu.ufpr.jmetal.algorithm.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.uma.jmetal.runner.AbstractAlgorithmRunner;

import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.jmetal.problem.fitness.FitnessFunction;
import edu.ufpr.jmetal.problem.fitness.SilhouetteFitness;
import edu.ufpr.jmetal.problem.old.impl.DataInstanceReader;
import edu.ufpr.math.utils.MathUtils;

public class ClusteringGrammaticalEvolutionExperiment extends AbstractAlgorithmRunner {

    private static final String ILLEGAL_ARGUMENT_MSG = "The %s argument isn't supported";

    public static void main(String[] args) throws FileNotFoundException, IOException {

        if (args.length < 30) {
            System.err.println("Usage: java -jar ge-clustering-jar-with-dependencies.jar "
                + "-g [grammar file] -d [database] -dt [data type] -m [max evaluations] -r [result folder]"
                + " -s [seed] -p [populationSize]  -minC [minCondons] -maxC [maxCondons] -t [threads pool size]"
                + "-cx [crossover probabilty] -mx [mutation probability] -pi [prune index] -px [prune probability] "
                + "-dx [duplication probability] -cs [number of clustering seeds] -dt [dataType] -mc [max clustering executions] \n");
            System.exit(1);
        }

        String resultDirName = null;

        String grammarFile = null;
        String databaseFile = null;
        double crossoverProbability = 0.95;
        double mutationProbability = 0.1;
        double pruneMutationProbability = 0.05;
        double duplicationProbability = 0.05;
        int pruneIndex = 10;
        int maxEvaluations = 0;
        int populationSize = 100;
        int clusteringExecutionSeed = 100;
        int seed = 0;
        int minCondons = 1;
        int maxCondons = 20;
        int threadPoolSize = 0;
        String dataType = "";
        boolean classIncluded = true;
        int maxClusteringExecutions = 1000;
        int maxAlgorithmDepth = 20;

        for (int i = 0; i < args.length; i = i + 2) {
            String param = args[i];
            switch (param) {
                case "-g":
                    grammarFile = args[i + 1];
                    break;
                case "-d":
                    databaseFile = args[i + 1];
                    break;
                case "-m":
                    maxEvaluations = Integer.valueOf(args[i + 1]);
                    break;
                case "-r":
                    resultDirName = args[i + 1];
                    break;
                case "-s":
                    seed = Integer.valueOf(args[i + 1]);
                    break;
                case "-cx":
                    crossoverProbability = Double.valueOf(args[i + 1]);
                    break;
                case "-mx":
                    mutationProbability = Double.valueOf(args[i + 1]);
                    break;
                case "-pi":
                    pruneIndex = Integer.valueOf(args[i + 1]);
                    break;
                case "-px":
                    pruneMutationProbability = Double.valueOf(args[i + 1]);
                    break;
                case "-dx":
                    duplicationProbability = Double.valueOf(args[i + 1]);
                    break;
                case "-cs":
                    clusteringExecutionSeed = Integer.valueOf(args[i + 1]);
                    break;
                case "-minC":
                    minCondons = Integer.valueOf(args[i + 1]);
                    break;
                case "-maxC":
                    maxCondons = Integer.valueOf(args[i + 1]);
                    break;
                case "-p":
                    populationSize = Integer.valueOf(args[i + 1]);
                    break;
                case "-t":
                    threadPoolSize = Integer.valueOf(args[i + 1]);
                    break;
                case "-dt":
                    dataType = args[i + 1];
                    break;
                case "-ci":
                    classIncluded = Boolean.valueOf(args[i + 1]);
                    break;
                case "-mc":
                    maxClusteringExecutions = Integer.valueOf(args[i + 1]);
                    break;
                case "-md":
                    maxAlgorithmDepth = Integer.valueOf(args[i + 1]);
                    break;
                default:
                    throw new IllegalArgumentException(String.format(ILLEGAL_ARGUMENT_MSG, param));
            }
        }

        File resultDir = new File(resultDirName);
        if (!resultDir.exists()) {
            resultDir.mkdir();
        }

        List<Point> points =
            MathUtils.normalizeData(DataInstanceReader.readPoints(databaseFile, dataType, classIncluded));

        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
        FitnessFunction fitnessFunction = new SilhouetteFitness();

        GrammaticalEvolutionClusteringTask task = GrammaticalEvolutionClusteringTask.builder()
            .withClusteringExecutionSeed(clusteringExecutionSeed).withCrossoverProbability(crossoverProbability)
            .withDuplicationProbability(duplicationProbability).withFitnessFunction(fitnessFunction)
            .withGrammarFile(grammarFile).withMaxCondons(maxCondons).withMaxEvaluations(maxEvaluations)
            .withMinCondons(minCondons).withMutationProbability(mutationProbability).withPoints(points)
            .withPopulationSize(populationSize).withPruneIndex(pruneIndex)
            .withPruneMutationProbability(pruneMutationProbability).withResultDirName(resultDirName).withSeed(seed)
            .withMaxClusteringExecutions(maxClusteringExecutions).withMaxAlgorithmDepth(maxAlgorithmDepth).build();
        executorService.execute(task);
        executorService.shutdown();

    }

}
