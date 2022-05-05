package ru.itis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Centroid extends Point{

    public Centroid(Map<String, Double> coordinates) {
        super(coordinates);
    }

    public static Centroid fromPoint(Point p){
        return new Centroid(new HashMap<>(p.getCoordinates()));
    }

    public void recalculateCoordinates(List<Point> points) {

        Map<String, Double> newCoordinates = new HashMap<>();
        for (int i = 0; i < points.size(); i++) {
            for (Map.Entry<String, Double> e : points.get(i).getCoordinates().entrySet()) {
                final int iF = i + 1;
                newCoordinates.compute(e.getKey(), (k, v) -> {
                    if(v == null){
                        return e.getValue();
                    }

                    return (v*iF + e.getValue())/(iF + 1);
                });
            }
        }
        setCoordinates(newCoordinates);
    }
}
