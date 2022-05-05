package ru.itis;


import java.util.*;
import java.util.stream.Collectors;

public class KMeansClustering {

    private int k;
    private List<Point> points;
    private Map<Centroid, List<Point>> clusters;
    private List<Centroid> centroids = new ArrayList<>();
    private int maxIterations;

    public KMeansClustering(int k, List<Point> points, int maxIterations) {
        this.k = k;
        this.points = points;
        clusters = new HashMap<>();
        this.maxIterations = maxIterations;

        initCentroids();
    }

    public Map<Centroid, List<Point>> clusterize() {

        for (int i = 0; i < maxIterations; i++) {
            Map<Centroid, List<Point>> lastState = new HashMap<>(clusters);
            clusters = getClusters();

            for (Point point : points) {
                Centroid nearestCentroid = getNearestCentroid(point);
                clusters.get(nearestCentroid).add(point);
            }

            for (Centroid centroid : centroids) {
                centroid.recalculateCoordinates(clusters.get(centroid));
            }

            System.out.println(i);
            if (clusters.equals(lastState)) break;
        }

        return clusters;
    }

    private Map<Centroid, List<Point>> getClusters() {
        return centroids.stream().collect(Collectors.toMap(key -> key, v -> new ArrayList<>()));
    }


    private Centroid getNearestCentroid(Point point) {

        Centroid nearestCentroid = null;
        double distToNearestCentroid = Integer.MAX_VALUE;

        for (Centroid centroid : centroids) {
            double dist = point.countDistance(centroid);

            if (dist < distToNearestCentroid) {
                distToNearestCentroid = dist;
                nearestCentroid = centroid;
            }

        }
        return nearestCentroid;
    }

    private void initCentroids() {
        Point initPoint = points.get((int) (Math.random() * points.size()));

        Centroid c = Centroid.fromPoint(initPoint);

        clusters.put(c, new ArrayList<>(List.of(initPoint)));
        centroids.add(c);

        for (int i = 1; i < k; i++) {
            addAnotherCentroid();
        }
    }

    private void addAnotherCentroid() {
        double minDistance = 0;
        Point potentialCentroid = null;

        for (Point point : points) {
            if (point.isInCluster()) continue;

            double localMin = Integer.MAX_VALUE;

            for (Centroid centroid : clusters.keySet()) {
                double dist = point.countDistance(centroid);
                localMin = Math.min(dist, localMin);
            }

            if (minDistance < localMin) {
                minDistance = localMin;
                potentialCentroid = point;
            }
        }

        assert potentialCentroid != null;

        Centroid c = Centroid.fromPoint(potentialCentroid);

        centroids.add(c);
        clusters.put(c, new ArrayList<>(List.of(potentialCentroid)));
    }
}
