package edu.ufpr.ge.representation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Expression {

    protected int index;
    protected List<Node> nodes;

    public Expression() {
        this.nodes = new ArrayList<>();
    }

    public Expression(int index) {
        this();
        this.index = index;
    }

    public Expression(int index, List<Node> values) {
        this.index = index;
        this.nodes = values;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setValues(List<Node> values) {
        this.nodes = values;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.nodes);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Expression other = (Expression) obj;
        for (int i = 0; i < nodes.size(); i++) {
            Node node1 = nodes.get(i);
            Node node2 = other.getNodes().get(i);
            if (!node1.equals(node2)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return nodes.stream()
                .map((Node node) -> {
                    String result = node.isTerminal()
                            ? "\"" + node.getName() + "\""
                            : "<" + node.getName() + ">";
                    return result;
                }).collect(Collectors.joining(" "));
    }

}