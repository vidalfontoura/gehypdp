package edu.ufpr.cluster.algorithm;

import java.util.ArrayList;
import java.util.List;

public class Point {

    private Cluster cluster;

    private List<Double> coordinates;

    public Point() {
    }

    public Point(List<Double> coordinates) {
        setCoordinates(coordinates);
    }

    public Cluster getCluster() {

        return cluster;
    }

    public void setCluster(Cluster cluster) {

        this.cluster = cluster;
    }

    public List<Double> getCoordinates() {

        if (coordinates == null) {
            coordinates = new ArrayList<Double>();
        }
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {

        this.coordinates = coordinates;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((cluster == null) ? 0 : cluster.hashCode());
        result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point other = (Point) obj;
        // if (cluster == null) {
        // if (other.cluster != null)
        // return false;
        // } else if (!cluster.equals(other.cluster))
        // return false;
        if (coordinates == null) {
            if (other.coordinates != null)
                return false;
        } else if (!coordinates.equals(other.coordinates))
            return false;
        return true;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < coordinates.size(); i++) {
            sb.append(coordinates.get(i)).append(",");
        }
        return sb.deleteCharAt(sb.lastIndexOf(",")).append(")").toString();
    }

    public Point copy() {

        Point point = new Point();
        point.setCluster(cluster);
        point.coordinates = new ArrayList<Double>(coordinates);
        return point;
    }

    public void clearCluster() {

        this.cluster = null;
    }

}
