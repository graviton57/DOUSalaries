package com.havrylyuk.dou.utils.chart.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import static com.havrylyuk.dou.Config.JOB_SOFT;

/**
 * Created by Igor Havrylyuk on 29.09.2017.
 */

public class GroupBarValueFormatter implements IAxisValueFormatter {


    public GroupBarValueFormatter() {
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int v = (int) value;
        if (v >= 0 && v < JOB_SOFT.length) {
            return JOB_SOFT[((int) value)];
        }
        return "";
    }

}
