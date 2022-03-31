package ru.emdavl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    // Мб сделать динамический фильтр, тип изначально я не знаю какие тут числа
    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader("src/ru/emdavl/blabla.txt"));

        List<String> lines = reader.lines().flatMap(s -> {
                    s = s.replaceAll("\\p{Punct}", " ");
                    return Arrays.stream(s.split(" "));
                })
                .filter(e -> !e.isEmpty())
                .collect(Collectors.toList());

        reader.close();
        System.out.println(lines);
        BloomFilter bloomFilter = new BloomFilter(lines.size(), 0.1);
        for (String word : lines) {
            bloomFilter.add(word);
        }
        System.out.println(bloomFilter.exists(lines.get(0)));
        System.out.println(bloomFilter.exists("pepepe"));
    }
}
