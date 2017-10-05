package com.havrylyuk.dou.utils.chart.listviewitems;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.ChartData;
import com.havrylyuk.dou.R;
import com.havrylyuk.dou.utils.chart.ChartHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Pie Chart Item
 * Created by Igor Havrylyuk on 01.08.2017.
 */

public class PieChartItem extends ChartItem {

    private Typeface typeface;
    private SpannableString centerText;

    public PieChartItem(Context context, ChartData<?> cd,  String centerText) {
        super(cd);
        this.typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
       // this.typeface = ResourcesCompat.getF(this, R.fo.tangerine_regular)

        this.centerText = generateCenterText(centerText);
    }

    @Override
    public int getItemType() {
        return TYPE_PIE_CHART;
    }

    private SpannableString generateCenterText(String centerText) {
       /* SpannableString s = new SpannableString(centerText);
        s.setSpan(new RelativeSizeSpan(1.6f), 0, 14, 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.VORDIPLOM_COLORS[0]), 0, 14, 0);
        s.setSpan(new RelativeSizeSpan(.9f), 14, 25, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, 25, 0);
        s.setSpan(new RelativeSizeSpan(1.4f), 25, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 25, s.length(), 0);*/
        return new SpannableString(centerText);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder) {
        PieChartHolder pieHolder = (PieChartHolder) holder;
        ChartHelper.setPieChart(pieHolder.chart, getChartData(), centerText, typeface);
    }

    public static class PieChartHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.chart)
        public PieChart chart;

        public PieChartHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
