package ru.itis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Runner {
    public static void main(String[] args) {

        List<Point> points = processDataset();
        KMeansClustering clustering = new KMeansClustering(3, points, 20);

        for (Map.Entry<Centroid, List<Point>> e : clustering.clusterize().entrySet()) {
            System.out.println("-------------------------- CLUSTER ----------------------------");
            System.out.println("Centroid: " + e.getKey());
//            System.out.println("Points: " + e.getValue().toString());
            System.out.println("Missed: " + countMissed(e.getValue()));
            System.out.println();
            System.out.println();
        }
    }

    private static String countMissed(List<Point> value) {

        Map<SeedType, Long> collect = value.stream().map(Seed.class::cast).collect(Collectors.groupingBy(Seed::getSeedType, Collectors.counting()));
        StringBuilder res = new StringBuilder();
        for (SeedType type : collect.keySet()) {
            res.append("Type: ").append(type).append(" repeated: ").append(collect.get(type)).append("\n");
        }
        return res.toString();
    }

    private static List<Point> processDataset() {

        List<Point> points = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/seeds_dataset.csv"))){
            String c = reader.readLine();

            while((c = reader.readLine()) != null){
                String[] splitted = c.split(",");
                Seed s = new Seed(getFeatures(splitted), SeedType.values()[Integer.parseInt(splitted[8]) - 1], Integer.parseInt(splitted[0]));
                points.add(s);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return points;
    }

    private static Map<String, Double> getFeatures(String[] splitted) {
        Map<String, Double> features = new HashMap<>();

        features.put("area", Double.valueOf(splitted[1]));
        features.put("perimeter", Double.valueOf(splitted[2]));
        features.put("compactness", Double.valueOf(splitted[3]));
        features.put("lengthOfKernel", Double.valueOf(splitted[4]));
        features.put("widthOfKernel", Double.valueOf(splitted[5]));
        features.put("asymmetryCoefficient", Double.valueOf(splitted[6]));
        features.put("lengthOfKernelGroove", Double.valueOf(splitted[7]));

        return features;
    }
}
