package com.havrylyuk.dou.data.local.model;

/**
 * Created by Igor Havrylyuk on 23.09.2017.
 */

public class SalaryDataForWidget {

    private int q1;
    private int median;
    private int q3;
    private int salariesCount;

    public SalaryDataForWidget() {
    }

    public SalaryDataForWidget(int q1, int median, int q3, int salariesCount) {
        this.q1 = q1;
        this.median = median;
        this.q3 = q3;
        this.salariesCount = salariesCount;
    }

    public int getQ1() {
        return q1;
    }

    public void setQ1(int q1) {
        this.q1 = q1;
    }

    public int getMedian() {
        return median;
    }

    public void setMedian(int median) {
        this.median = median;
    }

    public int getQ3() {
        return q3;
    }

    public void setQ3(int q3) {
        this.q3 = q3;
    }

    public int getSalariesCount() {
        return salariesCount;
    }

    public void setSalariesCount(int salariesCount) {
        this.salariesCount = salariesCount;
    }
}
