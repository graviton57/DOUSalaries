package com.havrylyuk.dou.ui.main.demographics;

/**
 * Created by Igor Havrylyuk on 26.09.2017.
 */

public enum DemographicsType {

    AGE         ("Age"),
    EXPERIENCE  ("Experience"),
    GENDER      ("Gender"),
    ENG_LEVEL   ("English level"),
    CITY        ("City"),
    EDUCATION   (" Education"),
    COMPANY_SIZE("Company size"),
    COMPANY_TYPE("Type of company"),
    JOB_TITLE   ("Job title");

    private String name;

    DemographicsType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
