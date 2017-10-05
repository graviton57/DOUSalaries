package com.havrylyuk.dou.ui.main.by_cities;

import com.havrylyuk.dou.ui.base.Presenter;

/**
 * Created by Igor Havrylyuk on 25.09.2017.
 */

public interface ByCitiesMvpPresenter<V extends ByCitiesMvpView> extends Presenter<V> {

    void loadChartItems(long period);

}
