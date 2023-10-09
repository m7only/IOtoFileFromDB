package org.m7.util;

public class SumAvg {
    private Integer sum = 0;
    private Integer counter = 0;

    public Integer add(Integer add) {
        counter++;
        return sum += add;
    }

    public Integer getSum() {
        return sum;
    }

    public Double getAvg() {
        return ((double) sum) / counter;
    }
}
