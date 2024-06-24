package com.epam.jmp.api.models;

public class Stats {
    private double min;
    private double max;
    private double avg;
    private double sum;

    public Stats(double min, double max, double avg, double sum) {
        this.min = min;
        this.max = max;
        this.avg = avg;
        this.sum = sum;
    }

    public Stats() {
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "min=" + min +
                ", max=" + max +
                ", avg=" + avg +
                ", sum=" + sum +
                '}';
    }
}
