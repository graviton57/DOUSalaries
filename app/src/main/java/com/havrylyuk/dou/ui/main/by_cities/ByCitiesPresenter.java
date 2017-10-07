package com.havrylyuk.dou.ui.main.by_cities;

import com.havrylyuk.dou.data.IDataManager;
import com.havrylyuk.dou.data.remote.helper.CompositeDisposableHelper;
import com.havrylyuk.dou.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 *
 * Created by Igor Havrylyuk on 25.09.2017.
 */

public class ByCitiesPresenter<V extends ByCitiesMvpView> extends BasePresenter<V>
        implements ByCitiesMvpPresenter<V> {

    @Inject
    public ByCitiesPresenter(CompositeDisposableHelper compositeDisposableHelper,
                             IDataManager dataManager) {
        super(compositeDisposableHelper, dataManager);
    }

    @Override
    public void loadChartItems(long period) {
        getCompositeDisposableHelper()
                .addDisposable(getDataManager().getSalaryForCities(period)
                        .compose(getCompositeDisposableHelper().applySchedulers())
                        .subscribe(chartItems -> {
                            if (chartItems.isEmpty()) {
                                getMvpView().showEmptyView();
                            } else {
                                getMvpView().showSalaries(chartItems);
                            }

                        }));
    }
}

