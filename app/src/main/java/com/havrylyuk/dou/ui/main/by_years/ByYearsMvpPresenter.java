package com.havrylyuk.dou.ui.main.by_years;

import com.havrylyuk.dou.ui.base.Presenter;

/**
 * Created by Igor Havrylyuk on 25.09.2017.
 */

public interface ByYearsMvpPresenter<V extends ByYearsMvpView> extends Presenter<V> {

    void loadChartItems(String city);

}
