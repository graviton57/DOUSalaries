package com.havrylyuk.dou.utils.chart.listviewitems;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.havrylyuk.dou.R;
import com.havrylyuk.dou.utils.chart.ChartHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.havrylyuk.dou.Config.JOB_SOFT;

/**
 * Group Bar Chart Item
 * Created by Igor Havrylyuk on 06.09.2017.
 */

@SuppressWarnings("unused")
public class GroupBarChartItem extends ChartItem {

    private Typeface typeface;
    private Context context;
    private String language;
    //group fields
    private float groupSpace = 0.1f;
    private float barSpace = 0.03f; // x5 DataSet
    private float barWidth = 0.15f; // x5 DataSet
    // (0.15 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

    private int groupCount = JOB_SOFT.length;//Senior, engineer, junior
    private int start;

    public GroupBarChartItem(Context context, ChartData<?> chartData ) {
        super(chartData);
        this.context = context;
        this.typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        this.language = "";
        this.start = 0;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public float getGroupSpace() {
        return groupSpace;
    }

    public void setGroupSpace(float groupSpace) {
        this.groupSpace = groupSpace;
    }

    public float getBarSpace() {
        return barSpace;
    }

    public void setBarSpace(float barSpace) {
        this.barSpace = barSpace;
    }

    public float getBarWidth() {
        return barWidth;
    }

    public void setBarWidth(float barWidth) {
        this.barWidth = barWidth;
    }

    public int getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public int getItemType() {
        return TYPE_GRP_BAR_CHART;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder ) {
        final BarChartHolder barHolder = (BarChartHolder)holder;
        barHolder.language.setText(language);
        ChartHelper.setGroupBarChart(context, barHolder.chart, getChartData(), typeface);
        // specify the width each bar should have
        barHolder.chart.getBarData().setBarWidth(barWidth);
        // restrict the x-axis range
        barHolder.chart.getXAxis().setAxisMinimum(start);
        // barData.getGroupWith(...) is a helper that calculates the width each group needs
        // based on the provided parameters
        barHolder.chart.getXAxis().setAxisMaximum(start + barHolder.chart.getBarData()
                .getGroupWidth(groupSpace, barSpace) * groupCount);
        barHolder.chart.groupBars(start, groupSpace, barSpace);
        barHolder.chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null){
                    return;
                }
                barHolder.chart.setDrawMarkers(true);
            }

            @Override
            public void onNothingSelected() {
                Timber.d("Nothing selected.");
            }
        });
    }


    public static class BarChartHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.chart)
        public BarChart chart;
        @BindView(R.id.item_title)
        TextView language;

        public BarChartHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
