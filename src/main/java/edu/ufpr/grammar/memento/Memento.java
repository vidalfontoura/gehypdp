package edu.ufpr.grammar.memento;

public interface Memento<T> {
    
    public void restore(T originator);
    
    public void setState(T originator);
}
