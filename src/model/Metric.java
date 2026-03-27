package model;

//represents a single measurement metric with its properties and raw value
 // example object: new Metric("SUS score", 50, true, 0, 100, "points", 89.0)

public class Metric {
    private final String name;
    private final int coefficient;
    private final boolean higherIsBetter;
    private final double rangeMin;
    private final double rangeMax;
    private final String unit;
    private final double rawValue;

    public Metric(String name, int coefficient, boolean higherIsBetter, double rangeMin, double rangeMax, String unit, double rawValue) {
        this.name = name;
        this.coefficient = coefficient;
        this.higherIsBetter = higherIsBetter;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        this.unit = unit;
        this.rawValue = rawValue;
    }

    
    //Calculates score (1,5) from raw value using formula
    //(Result is rounded to nearest 0.5)
     
    public double calculateScore() {
        double range = rangeMax - rangeMin;
        if (range == 0) return 3.0;
        double score;
        if (higherIsBetter) {
            score = 1.0 + (rawValue - rangeMin) / range * 4.0;
        } else {
            score = 5.0 - (rawValue - rangeMin) / range * 4.0;
        }
        score = Math.max(1.0, Math.min(5.0, score));
        return Math.round(score * 2.0) / 2.0;
    }
/*
\u2191 → ↑ (yukarı ok)
\u2013 → – (tire, dash)
\u2193 → ↓ (aşağı ok) 
*/
    public String getDirectionLabel() {
        return higherIsBetter ? "Higher \u2191" : "Lower \u2193";
    }

    public String getRangeLabel() {
        if (rangeMin == (int) rangeMin && rangeMax == (int) rangeMax) {
            return (int) rangeMin + "\u2013" + (int) rangeMax;
        }
        return rangeMin + "\u2013" + rangeMax;
    }

    public String getName(){
        
        return name;
    }

    public int getCoefficient(){
        return coefficient;
    }

    public boolean isHigherIsBetter(){
        return higherIsBetter;
    }
    
    public double getRangeMin(){
        return rangeMin;
    }

    public double getRangeMax(){
        return rangeMax;
    }

    public String getUnit(){
        return unit;
    }

    public double getRawValue(){
        return rawValue;
    }
}
