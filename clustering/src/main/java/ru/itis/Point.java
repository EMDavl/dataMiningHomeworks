package ru.itis;

import java.util.Map;


public class Point {
    private Map<String, Double> coordinates;

    private boolean inCluster;

    public Point(Map<String, Double> coordinates) {
        this.coordinates = coordinates;
    }

    public Map<String, Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Map<String, Double> coordinates) {
        this.coordinates = coordinates;
    }

    public Double countDistance(Point p2){
        double dist = 0d;

        for (String key : coordinates.keySet()) {
            Double c1 = coordinates.get(key);
            Double c2 = p2.coordinates.get(key);
            if(c1 != null && c2 != null) {
                dist += Math.pow((c1 - c2), 2);
            }
        }

        return Math.sqrt(dist);
    }

    public boolean isInCluster() {
        return inCluster;
    }

    public void setInCluster(boolean inCluster) {
        this.inCluster = inCluster;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Map.Entry<String, Double> e : coordinates.entrySet()) {
            b.append("\t").append(e.getKey()).append(" ").append(e.getValue()).append("\n");
        }
        return b.toString();
    }
}
