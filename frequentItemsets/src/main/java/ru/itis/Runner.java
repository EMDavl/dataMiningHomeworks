package ru.itis;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Runner {

    public static void main(String[] args) {
        try {
            System.setOut(new PrintStream(new FileOutputStream("text.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Algorithm algorithm = new Algorithm(2);
        algorithm.compute();
        algorithm.printResults();
    }

}
