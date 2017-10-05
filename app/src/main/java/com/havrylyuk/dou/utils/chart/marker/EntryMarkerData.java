package com.havrylyuk.dou.utils.chart.marker;

/**
 * Created by Igor Havrylyuk on 01.10.2017.
 */

public class EntryMarkerData {

    private float value;
    private int count;

    public EntryMarkerData(float value, int count) {
        this.value = value;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
