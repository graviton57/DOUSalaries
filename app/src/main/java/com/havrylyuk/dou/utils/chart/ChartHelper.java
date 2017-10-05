package com.havrylyuk.dou.utils.chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.havrylyuk.dou.R;
import com.havrylyuk.dou.utils.chart.formatter.GroupBarValueFormatter;
import com.havrylyuk.dou.utils.chart.formatter.HorizontalBarValueFormatter;
import com.havrylyuk.dou.utils.chart.formatter.LineChartValueFormatter;
import com.havrylyuk.dou.utils.chart.marker.ExperienceMarker;
import com.havrylyuk.dou.utils.chart.marker.SalaryMarker;

import java.util.ArrayList;
import java.util.List;

/**
 * Charts Helper
 * Created by Igor Havrylyuk on 08.09.2017.
 */

public class ChartHelper {

    //Chart X and Y animation duration
    private static final int DURATION_LONG = 2500;
    private static final int DURATION_MEDIUM = 1500;
    private static final int DURATION_SHORT = 700;

    /**
     * Set the pie pattern
     * @param pieChart chart
     * @param chartData pie chart data
     * @param title chart title
     * @param tf Typeface font
     */
    public static void setPieChart(PieChart pieChart, ChartData<?> chartData,
                                   SpannableString title, Typeface tf) {
        chartData.setValueFormatter(new PercentFormatter());
        chartData.setValueTextSize(11f);
        chartData.setValueTextColor(Color.BLACK);
        chartData.setValueTypeface(tf);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setCenterTextTypeface(tf);
        pieChart.setCenterText(title);
        pieChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);// enable rotation of the chart by touch
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.setEntryLabelTextSize(10f);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);

        pieChart.setData((PieData) chartData);
        pieChart.animateY(DURATION_MEDIUM, Easing.EasingOption.EaseInOutQuad);
        pieChart.highlightValues(null);// undo all highlights
        pieChart.invalidate();

    }

    /**
     * Set the pie chart data source
     */
    public static PieData populatePieData(List<PieEntry> entries, String label){
        PieDataSet dataSet = new PieDataSet(entries, label);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        //dataSet.setColors(populateColors());// add a lot of colors
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
       // dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueTextSize(11f);
        return new PieData(dataSet);
    }

    /**
     * Set the line chart pattern
     * @param lineChart chart
     * @param chartData pie chart data
     * @param month data
     * @param typeface Typeface font
     */
    public static void setLineChart(Context context, LineChart lineChart, ChartData<?> chartData,
                                    final String[] month, Typeface typeface) {
        // apply styling
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        // create a custom MarkerView (extend MarkerView) and specify the layout to use for it
        SalaryMarker marker = new SalaryMarker(context, R.layout.marker_salary_detail);
        marker.setChartView(lineChart); // For bounds control
        lineChart.setMarker(marker);
        //fix crash com.github.mikephil.charting.charts.Chart.drawMarkers(Chart.java:731)
        lineChart.setDrawMarkers(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(typeface);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setValueFormatter(new LineChartValueFormatter(month));
        xAxis.setLabelCount(month.length / 2, true);//xAxis label count

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTypeface(typeface);
        leftAxis.setLabelCount(9, false);
        leftAxis.setAxisMinimum(0f);
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setTypeface(typeface);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f);
        Legend l = lineChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setTypeface(typeface);
        l.setFormSize(14f);
        l.setTextSize(9f);
        lineChart.setData((LineData) chartData);
        lineChart.animateX(DURATION_SHORT);

    }

    /**
     * Set the pie pattern
     * @param barChart chart
     * @param chartData pie chart data
     * @param context context
     * @param typeface Typeface font
     */
    public static void setGroupBarChart(Context context, BarChart barChart, ChartData<?> chartData,
                                     Typeface typeface) {
        barChart.getDescription().setEnabled(false);
        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);
        // create a custom MarkerView (extend MarkerView) and specify the layout to use for it
        SalaryMarker mv = new SalaryMarker(context, R.layout.marker_salary_detail);
        mv.setChartView(barChart); // For bounds control
        barChart.setMarker(mv); // Set the marker to the chart
        barChart.setDrawMarkers(false);

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setTypeface(typeface);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setTypeface(typeface);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(8f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new GroupBarValueFormatter());

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTypeface(typeface);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);
        barChart.getAxisRight().setEnabled(false);
        barChart.setData((BarData) chartData);
    }

    /**
     * Set the horizontal bar pattern
     * @param barChart chart
     * @param chartData horizontal bar chart data
     * @param jobs string array of job titles
     * @param typeface Typeface font
     */
    public static void setHorizontalBarChart(BarChart barChart, final ChartData<?> chartData,
                                             final String[] jobs, Typeface typeface) {
        barChart.setDrawGridBackground(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setLabelCount(chartData.getEntryCount());
        xAxis.setLabelCount(jobs.length);
        xAxis.setTypeface(typeface);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTypeface(typeface);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularity(1f);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(true);
        YAxis axisRight = barChart.getAxisRight();
        axisRight.setTypeface(typeface);
        axisRight.setDrawAxisLine(true);
        axisRight.setDrawGridLines(false);
        axisRight.setGranularity(1f);
        axisRight.setAxisMinimum(0f);

        final Legend legend = barChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setFormSize(8f);
        legend.setXEntrySpace(4f);
        barChart.setData((BarData) chartData);
        barChart.setFitBars(true);
        barChart.animateY(DURATION_LONG);
        xAxis.setValueFormatter(new HorizontalBarValueFormatter(jobs));
    }

    /**
     * Set the vertical bar  pattern
     * @param barChart chart
     * @param chartData pie chart data
     * @param typeface Typeface font
     */
    public static void setVerticalBarChart(Context context, BarChart barChart, ChartData<?> chartData,
                                            Typeface typeface) {
        // create a custom MarkerView (extend MarkerView) and specify the layout to use for it
        ExperienceMarker marker = new ExperienceMarker(context, R.layout.marker_exp_age);
        marker.setChartView(barChart); // For bounds control
        barChart.setMarker(marker);
        //fix crash com.github.mikephil.charting.charts.Chart.drawMarkers(Chart.java:731)
        barChart.setDrawMarkers(false);

        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //Sets the number of labels for the x-axis (display all the x-axis values)
       // xAxis.setLabelCount(chartData.getEntryCount());
        xAxis.setTypeface(typeface);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTypeface(typeface);
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setTypeface(typeface);
        rightAxis.setSpaceTop(20f);
        rightAxis.setAxisMinimum(0f);
        chartData.setValueTypeface(typeface);
        barChart.setData((BarData) chartData);
        barChart.setFitBars(true);
        barChart.animateY(DURATION_SHORT);
    }

    /**
     * Set the bar chart data source
     */
    public static BarData populateBarData(List<BarEntry> entries, String label, List<Integer> colors){
        BarDataSet barDataSet = new BarDataSet(entries, label);
        if (colors != null) {
             barDataSet.setColors(colors);
        }
        barDataSet.setHighLightAlpha(255);
        return  new BarData(barDataSet);
    }

    public static List<Integer> populateColors(){
         ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        return colors;
    }
}
