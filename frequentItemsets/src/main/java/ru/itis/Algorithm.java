package ru.itis;

import java.io.*;
import java.util.*;
import java.util.function.BiFunction;

public class Algorithm {

    private final int SUPPORT_LVL;

    private Map<String, List<String>> basketsMap = new HashMap<>();
    private Map<String, Integer> productsMap = new HashMap<>();
    private Set<Product> productsWithIndices = new HashSet<>();
    private Set<List<String>> resultList = new HashSet<>();
    private int[] buckets;
    private BiFunction<Long, Long, Integer> linearHash = (e1, e2) -> (int) ((e1 + e2) % productsMap.size());


    public Algorithm(int SUPPORT_LVL) {
        this.SUPPORT_LVL = SUPPORT_LVL;
    }

    public void compute() {
        readData();
        processProductsToResList();
        processDoubletonesToBucket();
        processDoubletonesToResList();
    }

    private void processDoubletonesToResList() {
        for (List<String> value : basketsMap.values()) {
            for (int i = 0; i < value.size() - 1; i++) {
                if(productsMap.get(value.get(i)) < SUPPORT_LVL) continue;
                long firstItemId = getProductIdByName(value.get(i));

                for (int j = i + 1; j < value.size(); j++) {
                    if(productsMap.get(value.get(j)) < SUPPORT_LVL) continue;

                    long secondItemId = getProductIdByName(value.get(j));
                    int bucket = linearHash.apply(firstItemId, secondItemId);

                    if(buckets[bucket] > SUPPORT_LVL){
                        resultList.add(List.of(value.get(i), value.get(j)));
                    }
                }
            }
        }
    }


    private void processProductsToResList() {
        for (Map.Entry<String, Integer> e : productsMap.entrySet()) {
            addToIndiced(e.getKey());
            if (e.getValue() >= SUPPORT_LVL) {
                resultList.add(List.of(e.getKey()));
            }
        }
    }

    private void addToIndiced(String key) {
        if (!productsWithIndices.contains(new Product(key, 0))) {
            productsWithIndices.add(Product.of(key));
        }
    }

    private void processDoubletonesToBucket() {

        for (List<String> value : basketsMap.values()) {
            for (int i = 0; i < value.size() - 1; i++) {

                long firstItemId = getProductIdByName(value.get(i));

                for (int j = i + 1; j < value.size(); j++) {

                    long secondItemId = getProductIdByName(value.get(j));
                    int bucket = linearHash.apply(firstItemId, secondItemId);

                    buckets[bucket] = 1 + buckets[bucket];
                }
            }
        }
    }

    private long getProductIdByName(String s) {
        return productsWithIndices.stream()
                .filter(e -> e.getProductName().equals(s))
                .findFirst()
                .get()
                .getpId();
    }

    public void printResults() {
        for (List<String> strings : resultList) {
            System.out.println(strings);
        }
    }

    private void readData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/transactions.csv"))) {
            // Skipping first line
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] separatedLine = line.split(";");
                processLine(separatedLine[0], separatedLine[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        buckets  = new int[productsMap.size()];
    }

    private void processLine(String productName, String basketId) {
        productsMap.compute(productName, ((s, integer) -> integer == null ? 1 : integer + 1));

        if (basketsMap.containsKey(basketId)) {
            basketsMap.get(basketId).add(productName);
        } else {
            basketsMap.put(basketId, new ArrayList<>(List.of(productName)));
        }
    }
}
