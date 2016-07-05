package edu.ufpr.cluster.algorithms.functions;

@FunctionalInterface
public interface Function<T> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     */
    void apply(T t);
}
