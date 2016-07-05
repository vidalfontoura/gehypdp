package edu.ufpr.ge.mapper.impl;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.ufpr.ge.mapper.AbstractGrammarMapper;
import edu.ufpr.ge.representation.Expression;
import edu.ufpr.ge.representation.Node;

public class SymbolicExpressionGrammarMapper extends AbstractGrammarMapper<String> {

	protected int currentIndex;
	protected int numberOfWraps;
	protected int currentDepth;
	protected List<Node> visitedNodes;

	protected int maxDepth;

	public SymbolicExpressionGrammarMapper() {
		super();
		maxDepth = 100;
	}

	public SymbolicExpressionGrammarMapper(Node rootNode) {
		super(rootNode);
	}

	public SymbolicExpressionGrammarMapper(int maxNumberOfSelfLoops) {
		this.maxDepth = maxNumberOfSelfLoops;
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
	public String interpret(List<Integer> grammarInstance) {
		currentIndex = 0;
		numberOfWraps = 0;
		currentDepth = 1;
		visitedNodes = new ArrayList<>();
		return getNodeValue(rootNode, grammarInstance);
	}

	private String getNodeValue(Node node, List<Integer> grammarInstance) {
		if (node.isTerminal()) {
			return node.getName();
		} else {
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
	}

	public static void main(String[] args) {
		SymbolicExpressionGrammarMapper mapper = new SymbolicExpressionGrammarMapper();
		mapper.loadGrammar("/symbolicexpression.bnf");

		List<Integer> integerList = new ArrayList<>();

		// for(int j=0; j<100; j++) {
		// JMetalRandom random = JMetalRandom.getInstance();
		// integerList = new ArrayList<>();
		// int maxBound = random.nextInt(20, 30);
		// for (int i = 0; i < maxBound; i++) {
		// integerList.add(random.nextInt(0, 1000));
		// }
		// System.out.println(integerList.toString());
		List<Integer> grammarInstance = Lists.newArrayList(764, 10, 712, 329, 187, 79, 286, 177, 54, 547, 928, 482, 491,
				556, 418, 182);
		System.out.println(mapper.interpret(grammarInstance));

		// }
	}

}