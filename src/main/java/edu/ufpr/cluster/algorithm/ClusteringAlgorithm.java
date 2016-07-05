package edu.ufpr.cluster.algorithm;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.ufpr.cluster.algorithms.functions.Function;
import edu.ufpr.cluster.algorithms.functions.InitializiationFunction;

public class ClusteringAlgorithm {

    private InitializiationFunction initilization;

    private List<Function<ClusteringContext>> functions;

    private ClusteringContext clusteringContext;

    private java.util.function.Function<List<Point>, Double> distanceFunction;

    private boolean logExecution = false;

    private List<Point> points;

    private int maxEvaluations;

    public ClusteringAlgorithm(InitializiationFunction initialization, List<Function<ClusteringContext>> functions,
        java.util.function.Function<List<Point>, Double> distanceFunction) {
        this.initilization = initialization;
        this.functions = functions;
        this.distanceFunction = distanceFunction;
        this.maxEvaluations = 1000;

    }

    public int getMaxEvaluations() {

        return maxEvaluations;
    }

    public void setMaxEvaluations(int maxEvaluations) {

        this.maxEvaluations = maxEvaluations;
    }

    public ClusteringContext execute() {

        this.clusteringContext = new ClusteringContext(points, distanceFunction);
        int evaluations = 0;

        // Call to the initialization functions of the centroids
        initilization.apply(clusteringContext);

        if (logExecution) {
            System.out.println("---------------Starting executiion-------------------");
            System.out.println("Total clusters: " + clusteringContext.getClusters().size());
            clusteringContext.getClusters().stream().forEach(c -> c.printCluster());
        }

        boolean finish = false;
        while (!finish) {
            List<Point> lastCentroids =
                clusteringContext.getClusters().stream().map(c -> c.getCentroid()).collect(Collectors.toList());

            for (Function<ClusteringContext> function : functions) {
                function.apply(clusteringContext);
            }
            evaluations++;

            List<Point> currentCentroids =
                clusteringContext.getClusters().stream().map(c -> c.getCentroid()).collect(Collectors.toList());

            double distance = 0;
            if (lastCentroids.size() == currentCentroids.size()) {
                for (int i = 0; i < lastCentroids.size(); i++) {
                    Point last = lastCentroids.get(i);
                    Point current = currentCentroids.get(i);
                    distance += distanceFunction.apply(Lists.newArrayList(last, current));
                }
            } else {
                distance = Double.MAX_VALUE;
            }

            if (distance == 0 || evaluations > maxEvaluations) {
                finish = true;
            }
            if (logExecution) {
                System.out.println("Interaction: " + evaluations);
                System.out.println("Total clusters: " + clusteringContext.getClusters().size());
                clusteringContext.getClusters().stream().forEach(c -> c.printCluster());
            }

        }

        // Iterate to clusters list and remove the ones without points
        List<Cluster> clusters = clusteringContext.getClusters();
        List<Cluster> emptyCluster = new ArrayList<Cluster>();
        for (int i = 0; i < clusters.size(); i++) {
            Cluster cluster = clusters.get(i);
            if (cluster.getPoints().isEmpty()) {
                emptyCluster.add(cluster);
            }
        }

        for (Cluster cluster : emptyCluster) {
            clusters.remove(cluster);
        }
        clusteringContext.setEvaluationsCount(evaluations);
        return clusteringContext;
    }

    public List<Point> getPoints() {

        return points;
    }

    public void setPoints(List<Point> points) {

        this.points = points;
    }

    public InitializiationFunction getInitilization() {

        return initilization;
    }

    public void setInitilization(InitializiationFunction initilization) {

        this.initilization = initilization;
    }

    public List<Function<ClusteringContext>> getFunctions() {

        return functions;
    }

    public void setFunctions(List<Function<ClusteringContext>> functions) {

        this.functions = functions;
    }

    public ClusteringContext getClusteringContext() {

        return clusteringContext;
    }

    public void setClusteringContext(ClusteringContext clusteringContext) {

        this.clusteringContext = clusteringContext;
    }

    public java.util.function.Function<List<Point>, Double> getDistanceFunction() {

        return distanceFunction;
    }

    public void setDistanceFunction(java.util.function.Function<List<Point>, Double> distanceFunction) {

        this.distanceFunction = distanceFunction;
    }

    public int getInitialK() {

        return this.getInitilization().getInitialK();
    }

    public void clearClusteringContext() {

        clusteringContext.setClusters(null);
        clusteringContext.getPoints().stream().forEach(p -> p.setCluster(null));
    }

    @Override
    public int hashCode() {

        final int prime = 31;

        return 31;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ClusteringAlgorithm other = (ClusteringAlgorithm) obj;

        List<Function<ClusteringContext>> otherFunctions = other.getFunctions();

        if (distanceFunction == null) {
            if (other.distanceFunction != null)
                return false;
        } else if (!distanceFunction.toString().equals(other.distanceFunction.toString()))
            return false;

        if (initilization == null) {
            if (other.initilization != null)
                return false;
        } else if (!initilization.toString().equals(other.initilization.toString()))
            return false;

        if (initilization.getInitialK() != other.initilization.getInitialK()) {
            return false;
        }

        if (functions == null) {
            if (other.functions != null)
                return false;
        } else if (functions.size() != otherFunctions.size()) {
            return false;
        }

        boolean equalsFunctions = false;
        for (int i = 0; i < functions.size(); i++) {
            equalsFunctions = functions.get(i).toString().equals(otherFunctions.get(i).toString());

            if (!equalsFunctions) {
                return false;
            }
        }

        return true;
    }

}
