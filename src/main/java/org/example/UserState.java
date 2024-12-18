package org.example;

import java.util.ArrayList;
import java.util.List;

public class UserState {
    private final long chatId;
    private int step = 0; // Текущий шаг диалога
    private String func;
    private String region;
    private int dimensions;
    private final List<Double> lowerBounds = new ArrayList<>();
    private final List<Double> upperBounds = new ArrayList<>();
    private int currentDimension = 0;
    private int N;

    public UserState(long chatId) {
        this.chatId = chatId;
    }

    public long getChatId() {
        return chatId;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getDimensions() {
        return dimensions;
    }

    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    public double[][] getBounds() {
        double[][] bounds = new double[dimensions][2];
        for (int i = 0; i < dimensions; i++) {
            bounds[i][0] = lowerBounds.get(i);
            bounds[i][1] = upperBounds.get(i);
        }
        return bounds;
    }

    public void addLowerBound(double lowerBound) {
        lowerBounds.add(lowerBound);
    }

    public void addUpperBound(double upperBound) {
        upperBounds.add(upperBound);
    }

    public int getCurrentDimension() {
        return currentDimension;
    }

    public void incrementDimension() {
        currentDimension++;
    }

    public int getN() {
        return N;
    }

    public void setN(int N) {
        this.N = N;
    }
}
