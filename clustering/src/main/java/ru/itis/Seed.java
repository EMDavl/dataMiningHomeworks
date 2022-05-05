package ru.itis;

import java.util.Map;

public class Seed extends Point{

    private SeedType seedType;
    private int seedId;

    public Seed(Map<String, Double> features, SeedType seedType, int seedId) {
        super(features);
        this.seedType = seedType;
        this.seedId = seedId;
    }

    @Override
    public String toString() {
        return "Type: " + seedType + " ID: " + seedId + "\n";
    }

    public SeedType getSeedType() {
        return seedType;
    }

    public void setSeedType(SeedType seedType) {
        this.seedType = seedType;
    }

    public int getSeedId() {
        return seedId;
    }

    public void setSeedId(int seedId) {
        this.seedId = seedId;
    }
}
