package ru.itis.metrics;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Map;

public class JacardCoef {

    private Map<String, List<String>> metrics;

    public JacardCoef(Map<String, List<String>> metrics) {
        this.metrics = metrics;
    }

    public void calculate(List<String> textWords) {
        System.out.println("========== JACARD =============");
        for (Map.Entry<String, List<String>> e : metrics.entrySet()) {
            System.out.println(e.getKey() + " " + countJacard(textWords, e.getValue()).round(new MathContext(2)));
        }
    }

    private BigDecimal countJacard(List<String> text, List<String> base) {
        double sameElements = 0;
        int totalElems = text.size() + base.size();
        if (text.size() < base.size()){
            List<String> temp = base;
            base = text;
            text = temp;
        }

        for (String word : base) {
            if(text.contains(word)){
                sameElements++;
            }
        }

        return BigDecimal.valueOf((sameElements / (totalElems - sameElements)));
    }
}
