package edu.ufpr.ge.mapper.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import edu.ufpr.cluster.algorithm.ClusteringAlgorithm;
import edu.ufpr.cluster.algorithm.ClusteringContext;
import edu.ufpr.cluster.algorithms.functions.DistanceFunction;
import edu.ufpr.cluster.algorithms.functions.Function;
import edu.ufpr.cluster.algorithms.functions.InitializiationFunction;
import edu.ufpr.cluster.algorithms.functions.impl.ChebyshevDistanceFunction;
import edu.ufpr.cluster.algorithms.functions.impl.EucledianDistanceFunction;
import edu.ufpr.cluster.algorithms.functions.impl.JoinClustersFunction;
import edu.ufpr.cluster.algorithms.functions.impl.ManhattanDistanceFunction;
import edu.ufpr.cluster.algorithms.functions.impl.MoveAveragePointFunction;
import edu.ufpr.cluster.algorithms.functions.impl.MoveBetweenClustersFunction;
import edu.ufpr.cluster.algorithms.functions.impl.MoveNearPointFunction;
import edu.ufpr.cluster.algorithms.functions.impl.RandomCentroidInitilization;
import edu.ufpr.cluster.algorithms.functions.impl.SplitClustersFunction;
import edu.ufpr.cluster.algorithms.functions.impl.UniformCentroidInitilization;
import edu.ufpr.cluster.random.ClusteringRandom;
import edu.ufpr.ge.mapper.AbstractGrammarMapper;
import edu.ufpr.ge.representation.Expression;
import edu.ufpr.ge.representation.Node;

public class ClusteringExpressionGrammarMapper extends AbstractGrammarMapper<ClusteringAlgorithm> {

    protected int currentIndex;
    protected int numberOfWraps;
    protected int currentDepth;
    protected List<Node> visitedNodes;

    protected int maxDepth;

    public ClusteringExpressionGrammarMapper(String grammarFile, int maxDepth) {
        loadGrammar(grammarFile);
        this.maxDepth = maxDepth;
    }

    public ClusteringExpressionGrammarMapper(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public ClusteringExpressionGrammarMapper(Node rootNode) {
        super(rootNode);
    }

    public int getMaxDepth() {

        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {

        this.maxDepth = maxDepth;
    }

    public int getNumberOfWraps() {

        return numberOfWraps;
    }

    @Override
    public ClusteringAlgorithm interpret(List<Integer> grammarInstance) {

        currentIndex = 0;
        numberOfWraps = 0;
        currentDepth = 1;
        visitedNodes = new ArrayList<>();
        String[] result = getNodeValue(rootNode, grammarInstance).split(" ");

        int k = Integer.valueOf(result[1]);
        if (k == 0) {
            // 5 is hardcoded need to change this in the future
            k = ClusteringRandom.getInstance().nextInt(2, 5);
        }
        String initializationValue = result[0];
        InitializiationFunction initializationFunction = null;
        switch (initializationValue) {
            case "uniform":
                initializationFunction = new UniformCentroidInitilization(k);
                break;
            case "random":
                initializationFunction = new RandomCentroidInitilization(k);
                break;
            default:
                throw new RuntimeException("Initialization " + initializationValue + " not supported");
        }

        String distance = result[2];

        DistanceFunction distanceFunction = null;
        switch (distance) {
            case "manhathan":
                distanceFunction = new ManhattanDistanceFunction();
                break;
            case "eucledian":
                distanceFunction = new EucledianDistanceFunction();
                break;
            case "chebyshev":
                distanceFunction = new ChebyshevDistanceFunction();
                break;
            default:
                throw new RuntimeException("Distance function " + distanceFunction + " not supported");
        }

        List<Function<ClusteringContext>> functions = new ArrayList<>();
        for (int i = 3; i < result.length; i++) {
            String opFunction = result[i];
            Function<ClusteringContext> function = null;
            switch (opFunction) {
                case "join":
                    function = new JoinClustersFunction();
                    break;
                case "split":
                    function = new SplitClustersFunction();
                    break;
                case "moveAverage":
                    function = new MoveAveragePointFunction();
                    break;
                case "moveBetween":
                    function = new MoveBetweenClustersFunction();
                    break;
                case "moveNear":
                    function = new MoveNearPointFunction();
                    break;
                default:
                    throw new RuntimeException("Operation function " + opFunction + " not supported");
            }
            functions.add(function);

        }

        ClusteringAlgorithm clusteringAlgorithm =
            new ClusteringAlgorithm(initializationFunction, functions, distanceFunction);
        return clusteringAlgorithm;

    }

    private String getNodeValue(Node node, List<Integer> grammarInstance) {

        if (node.isTerminal()) {
            return node.getName();
        }
        visitedNodes.add(node);

        int expressionsSize = node.getExpressions().size();
        if (expressionsSize > 1) {
            currentIndex++;
            if (currentIndex >= grammarInstance.size()) {
                currentIndex = 0;
                numberOfWraps++;
            }
        }

        int indexToGet = grammarInstance.get(currentIndex) % expressionsSize;
        Expression expression = node.getExpressions().get(indexToGet);
        for (Node childNode : expression.getNodes()) {
            if (visitedNodes.contains(childNode)) {
                currentDepth++;
                break;
            }
        }
        while (currentDepth > maxDepth) {
            currentDepth--;
            grammarInstance.set(currentIndex, grammarInstance.get(currentIndex) + 1);
            indexToGet = grammarInstance.get(currentIndex) % expressionsSize;
            expression = node.getExpressions().get(indexToGet);
            for (Node childNode : expression.getNodes()) {
                if (visitedNodes.contains(childNode)) {
                    currentDepth++;
                    break;
                }
            }
        }

        String result = expression.getNodes().stream().map(childNode -> getNodeValue(childNode, grammarInstance))
            .collect(Collectors.joining(" "));

        visitedNodes.remove(node);

        return result;

    }

    public static void main(String[] args) {

        ClusteringExpressionGrammarMapper mapper = new ClusteringExpressionGrammarMapper(20);
        mapper.loadGrammar("src/main/resources/clustergrammar.bnf");

        List<Integer> integerList = new ArrayList<>();

        for (int j = 0; j < 30; j++) {
            JMetalRandom random = JMetalRandom.getInstance();
            integerList = new ArrayList<>();
            int maxBound = random.nextInt(20, 300);
            for (int i = 0; i < maxBound; i++) {
                integerList.add(random.nextInt(0, 1000));
            }
            // System.out.println(integerList.toString());
            System.out.println(mapper.interpret(integerList));

        }
    }

}
