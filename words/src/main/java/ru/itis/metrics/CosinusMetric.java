package ru.itis.metrics;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Map;

public class CosinusMetric {
    private Map<String, List<String>> metrics;

    public CosinusMetric(Map<String, List<String>> metrics) {
        this.metrics = metrics;
    }

    public void calculate(List<String> textWords) {
        double[] textVector = new double[metrics.size()];
        fillTextVector(textVector, textWords);
        double textVectorPower = countPower(textVector);
        int i = 0;
        System.out.println("========== COSINUS =============");
        for (Map.Entry<String, List<String>> e : metrics.entrySet()) {
            System.out.println(e.getKey() + " " + countCosinus(textVector, textVectorPower, i).round(new MathContext(2)));
            i++;
        }
    }

    private void fillTextVector(double[] textVector, List<String> textWords) {
        int i = 0;
        for (List<String> value : metrics.values()) {
            for (String word : value){
                if(textWords.contains(word)) textVector[i]++;
            }
            i++;
        }
    }

    private BigDecimal countCosinus(double[] textVector, double textVectorPower, int axisNum) {
        int[] axisVector = new int[textVector.length];
        axisVector[axisNum] = 1;

        double res = (textVector[axisNum])/textVectorPower;

        return BigDecimal.valueOf(res);
    }

    private double countPower(double[] textVector) {

        double power = 0;
        for (double v : textVector) {
            power+=v*v;
        }

        return Math.sqrt(power);
    }
}
