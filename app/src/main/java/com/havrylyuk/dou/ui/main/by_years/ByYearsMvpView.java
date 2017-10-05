package com.havrylyuk.dou.ui.main.by_years;

import com.havrylyuk.dou.ui.base.BaseMvpView;
import com.havrylyuk.dou.utils.chart.listviewitems.ChartItem;

import java.util.List;

/**
 * Created by Igor Havrylyuk on 25.09.2017.
 */

public interface ByYearsMvpView extends BaseMvpView {

    void showSalaries(List<ChartItem> chartItems);

    void showEmptyView();

}
