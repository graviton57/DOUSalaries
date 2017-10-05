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

/**
 * Vertical Bar Chart Item
 * Created by Igor Havrylyuk on 07.08.2017.
 */

public class VerticalBarChartItem extends ChartItem {

    private Context context;
    private Typeface typeface;
    private String title;

    public VerticalBarChartItem(Context context, ChartData<?> cd) {
        super(cd);
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        this.context = context;
        this.title = "";
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getItemType() {
        return TYPE_VER_BAR_CHART;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder ) {
        final VerticalBarChartHolder barHolder = (VerticalBarChartHolder) holder;
        barHolder.title.setText(title);
        ChartHelper.setVerticalBarChart(context, barHolder.chart, getChartData(), typeface);
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

            }
        });
    }

    public static class VerticalBarChartHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.chart)
        public BarChart chart;
        @BindView(R.id.item_title)
        TextView title;

        public VerticalBarChartHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
