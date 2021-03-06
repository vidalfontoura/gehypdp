package edu.ufpr.grammar.mapper;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.ufpr.grammar.representation.Expression;
import edu.ufpr.grammar.representation.Node;

public class SymbolicExpressionGrammarMapper extends AbstractGrammarMapper<String> {

	protected int currentIndex;
	protected int numberOfWraps;
	protected int currentDepth;
	protected List<Node> visitedNodes;

	protected int maxDepth;

	public SymbolicExpressionGrammarMapper() {
		super();
		maxDepth = 10;
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
	protected synchronized String hookInterpret(List<Integer> grammarInstance) {
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
	}

	public static void main(String[] args) {
		SymbolicExpressionGrammarMapper mapper = new SymbolicExpressionGrammarMapper();

		mapper.loadGrammar("/selectiongrammar.bnf");
		List<Integer> grammarInstance = Lists.newArrayList(11, 20, 4, 24, 51, 24, 15, 12, 6, 92, 24, 52);
		String hookInterpret = mapper.hookInterpret(grammarInstance);
		System.out.println(hookInterpret);

		mapper.loadGrammar("/acceptancegrammar.bnf");
		grammarInstance = Lists.newArrayList(11, 20, 4, 24, 51, 24, 15, 12, 6, 92, 24, 52);
		hookInterpret = mapper.hookInterpret(grammarInstance);

		System.out.println(hookInterpret);

		System.out.println(grammarInstance.toString());
	}
}
