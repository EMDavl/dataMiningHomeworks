package ru.itis;

import java.util.Arrays;

public class DataMining {
    static int[][] matrix = new int[][]{{0, 1, 0, 0, 0, 0}, {0, 0, 1, 0, 0, 0}, {0, 0, 0, 0, 1, 0}, {0, 1, 0, 0, 0, 0}, {0, 0, 0, 1, 0, 1}, {0, 0, 0, 0, 0, 0}};
    static int[][] transp = new int[][]{{0, 0, 0, 0, 0, 0}, {1, 0, 0, 1, 0, 0}, {0, 1, 0, 0, 0, 0}, {0, 0, 0, 0, 1, 0}, {0, 0, 1, 0, 0, 0}, {0, 0, 0, 0, 1, 0}};

    public static void main(String[] args) {

        double[] hub = new double[]{1, 1, 1, 1, 1, 1};
        double[] auth = count(hub, transp);

        System.out.println("HUB: " + Arrays.toString(hub));
        System.out.println("AUTH: " + Arrays.toString(auth));

        for (int i = 0; i < 4; i++) {
            hub = count(auth, matrix);
            auth = count(hub, transp);

            System.out.println("HUB: " + Arrays.toString(hub));
            System.out.println("AUTH: " + Arrays.toString(auth));
        }
    }

    private static double[] count(double[] arr, int[][] matrix) {
        double scalingConst = 0;
        double[] res = new double[6];

        for (int i = 0; i < 6; i++) {
            double rowSum = 0;

            for (int j = 0; j < 6; j++) {
                rowSum += arr[j] * matrix[i][j];
            }
            res[i] = rowSum;
        }

        for (double d: res) {
            if (d > scalingConst){
                scalingConst = d;
            }
        }

        scalingConst = 1/scalingConst;

        for (int i = 0; i < res.length; i++) {
            res[i] = res[i] * scalingConst;
        }

        return res;
    }
}
