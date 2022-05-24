package ru.itis.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WordProcessor {

    private static Set<String> stopWords = getStopWords();

    private static Set<String> getStopWords() {
        Set<String> stopWords = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/stopWords.txt"))) {
            String line;
            while ((line = reader.readLine().strip()) != null) {
                stopWords.add(line);
            }
        } catch (Exception e) {
            // ignored
        }
        return stopWords;
    }
    // Регулярка для вычленения слов

    public List<String> processText(String path) {

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

            return reader.lines().flatMap(s -> {
                        s = s.replaceAll("\\p{Punct}", " ");
                        return Arrays.stream(s.split(" "));
                    })
                    .map(String::toLowerCase)
                    .filter(s -> !s.matches("\\d+") && !stopWords.contains(s))
                    .filter(e -> !e.isEmpty())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public List<String> getFromCsv(String path) {

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

            return reader.lines().flatMap(s -> Arrays.stream(s.split(","))).collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
