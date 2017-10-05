package com.havrylyuk.dou.utils.chart.listviewitems;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.havrylyuk.dou.R;
import com.havrylyuk.dou.utils.AppUtils;
import com.havrylyuk.dou.utils.chart.ChartHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Line Chart Item
 * Created by Igor Havrylyuk on 07.08.2017.
 */

public class LineChartItem extends ChartItem {

    private Context context;
    private Typeface typeface;
    private String title;
    private final String[] month;

    public LineChartItem(Context context, ChartData<?> chartData, String language) {
        super(chartData);
        this.typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        this.context = context;
        this.title = language;
        this.month = AppUtils.reverseArray(
                context.getResources().getStringArray(R.array.array_period_date));
    }

    @Override
    public int getItemType() {
        return TYPE_LINE_CHART;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder ) {
        final LineChartHolder lineHolder = (LineChartHolder) holder;
        lineHolder.language.setText(title);
        ChartHelper.setLineChart(context, lineHolder.chart, getChartData(), month, typeface);
        lineHolder.chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null){
                    return;
                }
                Timber.d("onValueSelected Selected:%s dataSet:%d", e.toString(), h.getDataSetIndex());
                lineHolder.chart.setDrawMarkers(true);
            }

            @Override
            public void onNothingSelected() {
                Timber.d("Nothing selected.");
            }
        });
    }

    public static class LineChartHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.chart)
        public LineChart chart;
        @BindView(R.id.item_title)
        TextView language;

        public LineChartHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
