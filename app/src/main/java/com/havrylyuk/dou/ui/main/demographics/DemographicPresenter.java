package com.havrylyuk.dou.ui.main.demographics;

import com.havrylyuk.dou.data.IDataManager;
import com.havrylyuk.dou.data.remote.helper.CompositeDisposableHelper;
import com.havrylyuk.dou.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 *
 * Created by Igor Havrylyuk on 25.09.2017.
 */

public class DemographicPresenter<V extends DemographicMvpView> extends BasePresenter<V>
        implements DemographicMvpPresenter<V> {

    @Inject
    public DemographicPresenter(CompositeDisposableHelper compositeDisposableHelper,
                                IDataManager dataManager) {
        super(compositeDisposableHelper, dataManager);
    }

    @Override
    public void loadDemographic(long period) {
        getCompositeDisposableHelper()
                .addDisposable(getDataManager()
                        .getSalaryForDemographic(period)
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
