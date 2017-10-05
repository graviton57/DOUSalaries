package com.havrylyuk.dou.utils.chart.listviewitems;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.ChartData;
import com.havrylyuk.dou.R;
import com.havrylyuk.dou.utils.AppUtils;
import com.havrylyuk.dou.utils.chart.ChartHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Horizontal Bar Chart Item
 * Created by Igor Havrylyuk on 10.09.2017.
 */

public class HorizontalBarChartItem extends ChartItem {

    private Typeface typeface;
    private final String[] jobTitles;
    private String title;

    public HorizontalBarChartItem(Context context, ChartData<?> chartData) {
        super(chartData);
        this.typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        this.jobTitles = AppUtils.reverseArray(
                context.getResources().getStringArray(R.array.array_job_title));
        this.title = "";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getItemType() {
        return TYPE_HOR_BAR_CHART;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder) {
        final HorizontalBarChartHolder barHolder = (HorizontalBarChartHolder)holder;
        barHolder.title.setText(title);
        ChartHelper.setHorizontalBarChart(barHolder.chart, getChartData(), jobTitles, typeface);
    }

    public static class HorizontalBarChartHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.hor_chart)
        public HorizontalBarChart chart;
        @BindView(R.id.chart_title)
        TextView title;

        public HorizontalBarChartHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
