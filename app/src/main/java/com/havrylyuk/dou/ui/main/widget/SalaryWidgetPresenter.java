package com.havrylyuk.dou.ui.main.widget;

import com.havrylyuk.dou.data.IDataManager;
import com.havrylyuk.dou.data.remote.helper.CompositeDisposableHelper;
import com.havrylyuk.dou.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 *
 * Created by Igor Havrylyuk on 23.09.2017.
 */

public class SalaryWidgetPresenter<V extends SalaryWidgetMvpView> extends BasePresenter<V>
        implements SalaryWidgetMvpPresenter<V> {

    @Inject
    public SalaryWidgetPresenter(CompositeDisposableHelper compositeDisposableHelper,
                                 IDataManager dataManager) {
        super(compositeDisposableHelper, dataManager);
    }

    @Override
    public void loadSalaries(long period, String language, String city,
                             String jobTitle, int experience) {

        getCompositeDisposableHelper().addDisposable(
                getDataManager().getSalaryForWidget(period, language, city, jobTitle, experience)
                        .compose(getCompositeDisposableHelper().applySchedulers())
                        .subscribe(salaryDataForWidget -> {
                            if (salaryDataForWidget == null) {
                                getMvpView().showEmptyView();
                            } else {
                                getMvpView().showSalaries(salaryDataForWidget);
                            }

                        }));
    }

}
