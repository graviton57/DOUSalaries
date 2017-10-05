package com.havrylyuk.dou.utils.chart.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Igor Havrylyuk on 29.09.2017.
 */

public class LineChartValueFormatter implements IAxisValueFormatter {

    private String[] month;

    public LineChartValueFormatter(String[] month) {
        this.month = month;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if ((int) value >= 0 && value < month.length) {
            return month[(int) value];
        }
        return "";
    }
}
