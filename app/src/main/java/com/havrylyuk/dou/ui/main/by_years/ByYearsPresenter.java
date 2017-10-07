package com.havrylyuk.dou.ui.main.by_years;

import com.havrylyuk.dou.data.IDataManager;
import com.havrylyuk.dou.data.remote.helper.CompositeDisposableHelper;
import com.havrylyuk.dou.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 *
 * Created by Igor Havrylyuk on 25.09.2017.
 */

public class ByYearsPresenter <V extends ByYearsMvpView> extends BasePresenter<V>
        implements ByYearsMvpPresenter<V> {

    @Inject
    public ByYearsPresenter(CompositeDisposableHelper compositeDisposableHelper, IDataManager dataManager) {
        super(compositeDisposableHelper, dataManager);
    }

    @Override
    public void loadChartItems(String city) {
        getCompositeDisposableHelper()
                .addDisposable(getDataManager()
                        .getSalaryForYears(city)
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
