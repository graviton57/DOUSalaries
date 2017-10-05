package com.havrylyuk.dou.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.havrylyuk.dou.R;
import com.havrylyuk.dou.utils.chart.listviewitems.ChartItem;
import com.havrylyuk.dou.utils.chart.listviewitems.GroupBarChartItem;
import com.havrylyuk.dou.utils.chart.listviewitems.HorizontalBarChartItem;
import com.havrylyuk.dou.utils.chart.listviewitems.LineChartItem;
import com.havrylyuk.dou.utils.chart.listviewitems.PieChartItem;
import com.havrylyuk.dou.utils.chart.listviewitems.VerticalBarChartItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Igor Havrylyuk on 21.09.2017.
 */

public class SalariesAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ChartItem> chartItems;

    @Inject
    public SalariesAdapter(Context context) {
        this.context = context;
        this.chartItems = new ArrayList<>();
    }

    public  void addChartItems(List<ChartItem> list){
        if (this.chartItems != null && list != null) {
            this.chartItems.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void addChartItem(ChartItem chartItem) {
        if (this.chartItems != null && chartItem != null) {
            this.chartItems.add(chartItem);
            notifyDataSetChanged();
        }
    }

    public void clear(){
        if (chartItems != null) {
            chartItems.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent instanceof RecyclerView) {
            if (viewType == ChartItem.TYPE_GRP_BAR_CHART) {
                View view = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_ver_barchart, parent, false);
                return new GroupBarChartItem.BarChartHolder(view);
            } else if (viewType == ChartItem.TYPE_VER_BAR_CHART) {
                View view = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_ver_barchart, parent, false);
                return new VerticalBarChartItem.VerticalBarChartHolder(view);
            } else if (viewType == ChartItem.TYPE_HOR_BAR_CHART) {
                View view = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_hor_barchart, parent, false);
                return new HorizontalBarChartItem.HorizontalBarChartHolder(view);
            } else if (viewType == ChartItem.TYPE_LINE_CHART){
                View view = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_linechart, parent, false);
                return new LineChartItem.LineChartHolder(view);
            } else {
                View view = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_piechart, parent, false);
                return new PieChartItem.PieChartHolder(view);
            }
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case ChartItem.TYPE_PIE_CHART:
            case ChartItem.TYPE_LINE_CHART:
            case ChartItem.TYPE_GRP_BAR_CHART:
            case ChartItem.TYPE_HOR_BAR_CHART:
            case ChartItem.TYPE_VER_BAR_CHART:
                ChartItem chartItem =  chartItems.get(position);
                chartItem.onBindViewHolder(holder);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return chartItems.get(position).getItemType();
    }

    @Override
    public int getItemCount() {
        return chartItems != null ? chartItems.size() : 0;
    }

}
