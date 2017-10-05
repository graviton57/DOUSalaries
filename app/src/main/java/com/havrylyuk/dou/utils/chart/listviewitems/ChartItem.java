package com.havrylyuk.dou.utils.chart.listviewitems;


import android.support.v7.widget.RecyclerView;

import com.github.mikephil.charting.data.ChartData;

/**
 * Base class of the chart-list items
 * Created by Igor Havrylyuk on 01.08.2017.
 */

public abstract class ChartItem {
    //chart item types
    public static final int TYPE_VER_BAR_CHART = 0;
    public static final int TYPE_HOR_BAR_CHART = 1;
    public static final int TYPE_GRP_BAR_CHART = 2;
    public static final int TYPE_LINE_CHART = 3;
    public static final int TYPE_PIE_CHART = 4;

    private ChartData<?> chartData;

    public ChartItem(ChartData<?> cd) {
        this.chartData = cd;
    }

    public abstract int getItemType();

    public ChartData<?> getChartData() {
        return chartData;
    }

    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder);
}
