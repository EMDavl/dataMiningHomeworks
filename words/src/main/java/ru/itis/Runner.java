package ru.itis;

import ru.itis.metrics.CosinusMetric;
import ru.itis.metrics.JacardCoef;
import ru.itis.services.WordProcessor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Runner {

    public static void main(String[] args) {
        WordProcessor wordProcessor = new WordProcessor();

        List<String> textWords = wordProcessor.processText("src/main/resources/text.txt");

        List<String> science = wordProcessor.getFromCsv("src/main/resources/science.csv");
        List<String> sport = wordProcessor.getFromCsv("src/main/resources/sport.csv");
        List<String> shopping = wordProcessor.getFromCsv("src/main/resources/shopping.csv");
        List<String> news = wordProcessor.getFromCsv("src/main/resources/news.csv");

        Map<String, List<String>> metrics = Map.of(
                "science", science,
                "sport", sport,
                "shopping", shopping,
                "news", news);

        JacardCoef jakardCoef = new JacardCoef(metrics);
        Map<String, BigDecimal> jacardRes = jakardCoef.calculate(textWords);

        CosinusMetric cosinusMetric = new CosinusMetric(metrics);
        Map<String, BigDecimal> cosinusRes = cosinusMetric.calculate(textWords);

        String maxKey = null;
        double maxVal = 0;

        for (Map.Entry<String, BigDecimal> e : cosinusRes.entrySet()) {
            BigDecimal sum = e.getValue().add(jacardRes.get(e.getKey()));
            if(maxVal < sum.doubleValue()){
                maxKey = e.getKey();
                maxVal = sum.doubleValue();
            }
        }

        System.out.println("======= RESULT =======");
        System.out.println("Nearest group: " + maxKey);
    }

}
