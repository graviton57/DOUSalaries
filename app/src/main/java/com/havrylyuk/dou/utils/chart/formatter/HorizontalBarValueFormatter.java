package com.havrylyuk.dou.utils.chart.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.havrylyuk.dou.Config;

/**
 * Created by Igor Havrylyuk on 29.09.2017.
 */

public class HorizontalBarValueFormatter implements IAxisValueFormatter {

    private String[] jobs;

    public HorizontalBarValueFormatter(String[] jobs) {
        this.jobs = jobs;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if ((int) value >= 0 && value < jobs.length) {
            String title = jobs[(int) value];
            if (title.length() > Config.JOBS_LENGTH) {
                title = title.substring(0, Config.JOBS_LENGTH);
            }
            return title;
        }
        return "";
    }
}
