package edu.ufpr.ge.mapper.impl;

import java.util.ArrayList;
import java.util.Arrays;
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
import edu.ufpr.cluster.algorithms.functions.impl.RandomCentroidInitilization;
import edu.ufpr.cluster.algorithms.functions.impl.SplitClustersFunction;
import edu.ufpr.cluster.algorithms.functions.impl.UniformCentroidInitilization;
import edu.ufpr.ge.mapper.AbstractGrammarMapper;
import edu.ufpr.ge.representation.Expression;
import edu.ufpr.ge.representation.Node;

public class BKP_ClusteringExpressionGrammarMapper extends AbstractGrammarMapper<ClusteringAlgorithm> {

	protected int currentIndex;
	protected int currentDepth;
	protected List<Node> visitedNodes;
	protected int numberOfWraps;
	protected int maxDepth;

	public BKP_ClusteringExpressionGrammarMapper(Node rootNode) {
		super(rootNode);
		this.currentIndex = 0;
		this.currentDepth = 1;
		visitedNodes = new ArrayList<>();
		this.maxDepth = 100;
	}

	@Override
	public ClusteringAlgorithm interpret(List<Integer> grammarInstance) {

		Node gpNode = nonTerminalNodes.get("GP"); // ou rootNode
		InitializiationFunction initilizationFunction = interpretInitilization(
				gpNode.getExpressions().get(0).getNodes().get(0), grammarInstance);

		System.out.println(initilizationFunction);

		DistanceFunction distanceFunction = interpretDistanceFunction(gpNode.getExpressions().get(0).getNodes().get(1),
				grammarInstance);

		System.out.println(distanceFunction);

		String[] commands = getNodeValue(gpNode.getExpressions().get(0).getNodes().get(2), grammarInstance).split(" ");

		System.out.println(Arrays.toString(commands));
		List<Function<ClusteringContext>> functions = interpretFunctions(commands, grammarInstance);

		ClusteringAlgorithm clusteringAlgorithm = new ClusteringAlgorithm(initilizationFunction, functions,
				distanceFunction);

		return clusteringAlgorithm;
	}

	private InitializiationFunction interpretInitilization(Node initializationNode, List<Integer> grammarInstance) {
		Expression selectedExpression = selectExpression(initializationNode, grammarInstance);

		String initializationValue = selectedExpression.getNodes().get(0).getName();
		InitializiationFunction initializationFunction = null;
		int initialK = 0;
		if (selectedExpression.getNodes().size() == 2) {
			Node kNode = selectedExpression.getNodes().get(1);
			Node size = selectExpression(kNode, grammarInstance).getNodes().get(0);
			initialK = Integer.valueOf(size.getName());
		}

		System.out.println(initialK);

		switch (initializationValue) {
		case "uniform":
			initializationFunction = new UniformCentroidInitilization(initialK);
			break;
		case "random":
			initializationFunction = new RandomCentroidInitilization(initialK);
			break;
		default:
			throw new RuntimeException("Initialization " + initializationValue + " not supported");
		}
		return initializationFunction;

	}

	private DistanceFunction interpretDistanceFunction(Node distanceNode, List<Integer> grammarInstance) {
		DistanceFunction distanceFunction = null;
		Expression selectedExpression = selectExpression(distanceNode, grammarInstance);

		String distance = selectedExpression.getNodes().get(0).getName();
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

		return distanceFunction;
	}

	private List<Function<ClusteringContext>> interpretFunctions(String[] commands, List<Integer> grammarInstance) {
		List<Function<ClusteringContext>> functions = new ArrayList<>();
		for (int i = 0; i < commands.length; i++) {
			String opFunction = commands[i];
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
				function = new MoveBetweenClustersFunction();
				break;
			default:
				throw new RuntimeException("Operation function " + opFunction + " not supported");
			}
			functions.add(function);
		}
		return functions;
	}

	private String getNodeValue(Node node, List<Integer> grammarInstance) {
		if (node.isTerminal()) {
			return node.getName();
		}
		visitedNodes.add(node);

		int numberOfExpressions = node.getExpressions().size();
		if (numberOfExpressions > 1) {
			currentIndex++;
			if (currentIndex >= grammarInstance.size()) {
				currentIndex = 0;
				numberOfWraps++;
			}
		}

		int indexToGet = grammarInstance.get(currentIndex) % numberOfExpressions;
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
			indexToGet = grammarInstance.get(currentIndex) % numberOfExpressions;
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

	private Expression selectExpression(Node node, List<Integer> grammarInstance) {
		Expression expression = node.getExpressions()
				.get(grammarInstance.get(currentIndex % grammarInstance.size()) % node.getExpressions().size());
		if (node.getExpressions().size() > 1) {
			currentIndex++;
		}
		return expression;
	}

	public static void main(String[] args) {
		BKP_ClusteringExpressionGrammarMapper mapper = new BKP_ClusteringExpressionGrammarMapper(null);
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
			mapper.interpret(integerList);

		}
	}

}
