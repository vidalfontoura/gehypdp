package edu.ufpr.ge.representation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Node {

    protected String name;
    protected List<Expression> expressions;

    public Node(String description) {
        this.name = description;
        this.expressions = new ArrayList<>();
    }

    public Node(String name, List<Expression> expressions) {
        this.name = name;
        this.expressions = expressions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<Expression> expressions) {
        this.expressions = expressions;
    }

    public boolean isTerminal() {
        return expressions.isEmpty();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.name);
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
        final Node other = (Node) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return isTerminal()
                ? "\"" + name + "\""
                : "<" + name + "> ::= " + expressions.stream()
                .map(Expression::toString)
                .collect(Collectors.joining(" | "));
    }

}