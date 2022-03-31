package ru.emdavl;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.ToLongFunction;

public class BloomFilter {
    private final List<ToLongFunction<String>> functions;
    private final int filterLength;

    private final int functionsCount;
    private final int[] filter;

    public BloomFilter(int inputSize, double falsePositiveRate) {
        double ln2 = Math.log(2);
        this.filterLength = (int) -Math.round(inputSize*Math.log(falsePositiveRate)/(ln2*ln2));
        this.functionsCount = (int) Math.round((((double) filterLength)/inputSize)*ln2);
        functions = new ArrayList<>();
        filter = new int[filterLength];

        generateFunctions();
    }

    private void generateFunctions() {
        for (int i = 0; i < functionsCount; i++){
            Random rand = new Random();
            ToLongFunction<String> func = word -> {
                BigInteger seed = BigInteger.probablePrime(4, rand);
                double res = 1;

                for (int j = 0; j < word.length(); j++) {
                    res = seed.longValue() * res + word.charAt(j);
                }

                return Math.round(res);
            };

            functions.add(func);
        }
    }

    public void add(String word){
        for (ToLongFunction<String> f : functions) {
            int index = (int) ( f.applyAsLong(word) % filterLength);
            if(filter[index] > 10) System.out.println(word);
            if(filter[index] == Integer.MAX_VALUE) continue;
            filter[index]++;
        }
    }

    public boolean exists(String word){
        for (ToLongFunction<String> f : functions) {
            int index = (int) (f.applyAsLong(word) % filterLength);
            if(filter[index] == 0) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return Arrays.toString(filter) + "\n" + filterLength + "\n" + functionsCount;
    }
}
