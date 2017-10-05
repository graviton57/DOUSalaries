package com.havrylyuk.dou.data.local.model;

/**
 * Created by Igor Havrylyuk on 23.08.2017.
 */

public class SalaryData {

    private String date;
    private int salariesCount;
    private String jobTitle;

    public SalaryData() {
    }

    public SalaryData(String date, int salariesCount, String jobTitle) {
        this.date = date;
        this.salariesCount = salariesCount;
        this.jobTitle = jobTitle;
    }

    public SalaryData(String date, int salariesCount) {
        this(date, salariesCount, null);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSalariesCount() {
        return salariesCount;
    }

    public void setSalariesCount(int salariesCount) {
        this.salariesCount = salariesCount;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
