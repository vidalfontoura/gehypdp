package edu.ufpr.ge.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.uma.jmetal.util.JMetalException;

import edu.ufpr.ge.designpatterns.Memento;
import edu.ufpr.ge.representation.Expression;
import edu.ufpr.ge.representation.Node;
import edu.ufpr.jmetal.solution.impl.VariableIntegerSolution;

public abstract class AbstractGrammarMapper<T> {

	public static final String BNF_MATCHER = "((?<=<)[^<>]+(?=>))|((?<=\\\").+(?=\\\")|\\(|\\))";
	public static final int NON_TERMINAL_NODE = 1;
	public static final int TERMINAL_NODE = 2;

	protected Node rootNode;
	protected HashMap<String, Node> nonTerminalNodes;
	protected HashMap<String, Node> terminalNodes;

	public AbstractGrammarMapper() {
		nonTerminalNodes = new HashMap<>();
		terminalNodes = new HashMap<>();
	}

	public AbstractGrammarMapper(Node rootNode) {
		this();
		this.rootNode = rootNode;
	}

	public Node getRootNode() {
		return rootNode;
	}

	public void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
	}

	// <editor-fold defaultstate="expanded" desc="Mapping Section">
	public abstract T interpret(List<Integer> grammarInstance);

	public T interpret(VariableIntegerSolution grammarInstance) {
		return interpret(grammarInstance.getVariables());
	}

	// public boolean loadGrammar(String grammarFilePath) {
	// return loadGrammar(new File());
	// }

	public boolean loadGrammar(String grammarFile) {
		Memento<AbstractGrammarMapper<T>> memento = this.createMemento();
		try (Scanner scanner = new Scanner(AbstractGrammarMapper.class.getResourceAsStream(grammarFile))) {
			this.nonTerminalNodes = new HashMap<>();
			this.rootNode = null;

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (line.contains("::=")) {
					String[] lineSplit = line.split("::=");

					String nodeDescription = lineSplit[0].trim().replaceAll("[<>]", "");
					if (!nodeDescription.isEmpty()) {
						Node node = getNonTerminalNode(nodeDescription);
						if (this.rootNode == null) {
							this.rootNode = node;
						}

						String[] expressions = lineSplit[1].split("\\|");

						for (int i = 0; i < expressions.length; i++) {
							String expressionString = expressions[i].trim();
							if (!expressionString.isEmpty()) {
								Expression expression = buildExpression(expressionString, i);
								// if
								// (!node.getExpressions().contains(expression))
								// {
								node.getExpressions().add(expression);
								// }
							}
						}
					}
				}
			}
			// } catch (FileNotFoundException ex) {
			// this.setMemento(memento);
			// throw new JMetalException("File is not a file or does not
			// exist.");
		} catch (Exception ex) {
			this.setMemento(memento);
			throw ex;
		}
		if (rootNode == null) {
			this.setMemento(memento);
			throw new JMetalException("I could not find any grammar in the file.");
		}
		return true;
	}

	private Expression buildExpression(String expressionString, int index) {
		Expression expression = new Expression(index);
		Pattern pattern = Pattern.compile(BNF_MATCHER);
		Matcher matcher = pattern.matcher(expressionString);
		while (matcher.find()) {
			String foundNode;
			int i = 0;
			do {
				foundNode = matcher.group(++i);
			} while (foundNode == null);
			Node node;
			switch (i) {
			case NON_TERMINAL_NODE:
				node = getNonTerminalNode(foundNode);
				break;
			case TERMINAL_NODE:
				node = getTerminalNode(foundNode);
				break;
			default:
				throw new JMetalException("This should not be happening. You fucked up your grammar file mate!");
			}
			expression.getNodes().add(node);
		}
		return expression;
	}

	private Node getNonTerminalNode(String nodeName) {
		if (!nonTerminalNodes.containsKey(nodeName)) {
			nonTerminalNodes.put(nodeName, new Node(nodeName));
		}
		return nonTerminalNodes.get(nodeName);
	}

	private Node getTerminalNode(String nodeName) {
		if (!terminalNodes.containsKey(nodeName)) {
			terminalNodes.put(nodeName, new Node(nodeName));
		}
		return terminalNodes.get(nodeName);
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Memento Section">
	public Memento<AbstractGrammarMapper<T>> createMemento() {
		ProductionRulesMemento memento = new ProductionRulesMemento();
		memento.setState(this);
		return memento;
	}

	public void setMemento(Memento<AbstractGrammarMapper<T>> memento) {
		memento.restore(this);
	}

	private class ProductionRulesMemento implements Memento<AbstractGrammarMapper<T>> {

		private HashMap<String, Node> nonTerminalNodes;
		private HashMap<String, Node> terminalNodes;
		private Node rootNode;

		@Override
		public void restore(AbstractGrammarMapper<T> originator) {
			originator.nonTerminalNodes = this.nonTerminalNodes;
			originator.terminalNodes = this.terminalNodes;
			originator.rootNode = this.rootNode;
		}

		@Override
		public void setState(AbstractGrammarMapper<T> originator) {
			this.nonTerminalNodes = originator.nonTerminalNodes;
			this.terminalNodes = originator.terminalNodes;
			this.rootNode = originator.rootNode;
		}

	}
	// </editor-fold>

}