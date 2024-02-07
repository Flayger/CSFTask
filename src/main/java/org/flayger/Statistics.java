package org.flayger;

public class Statistics {


    private int intMin;
    private int intMax;
    private double intAverage;
    private float floatMin;
    private float floatMax;
    private double floatAverage;
    private String minLengthString;
    private String maxLengthString;

    // Constructor
    public Statistics(int intMin, int intMax, double intAverage, float floatMin, float floatMax, double floatAverage, String minLengthString, String maxLengthString) {
        this.intMin = intMin;
        this.intMax = intMax;
        this.intAverage = intAverage;
        this.floatMin = floatMin;
        this.floatMax = floatMax;
        this.floatAverage = floatAverage;
        this.minLengthString = minLengthString;
        this.maxLengthString = maxLengthString;
    }


    public int getIntMin() {
        return intMin;
    }

    public void setIntMin(int intMin) {
        this.intMin = intMin;
    }

    public int getIntMax() {
        return intMax;
    }

    public void setIntMax(int intMax) {
        this.intMax = intMax;
    }

    public double getIntAverage() {
        return intAverage;
    }

    public void setIntAverage(double intAverage) {
        this.intAverage = intAverage;
    }

    public float getFloatMin() {
        return floatMin;
    }

    public void setFloatMin(float floatMin) {
        this.floatMin = floatMin;
    }

    public float getFloatMax() {
        return floatMax;
    }

    public void setFloatMax(float floatMax) {
        this.floatMax = floatMax;
    }

    public double getFloatAverage() {
        return floatAverage;
    }

    public void setFloatAverage(double floatAverage) {
        this.floatAverage = floatAverage;
    }

    public String getMinLengthString() {
        return minLengthString;
    }

    public void setMinLengthString(String minLengthString) {
        this.minLengthString = minLengthString;
    }

    public String getMaxLengthString() {
        return maxLengthString;
    }

    public void setMaxLengthString(String maxLengthString) {
        this.maxLengthString = maxLengthString;
    }
}
