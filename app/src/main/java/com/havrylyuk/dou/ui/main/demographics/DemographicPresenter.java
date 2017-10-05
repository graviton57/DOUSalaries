package com.havrylyuk.dou.ui.main.demographics;

import com.havrylyuk.dou.data.IDataManager;
import com.havrylyuk.dou.data.remote.helper.CompositeDisposableHelper;
import com.havrylyuk.dou.ui.base.BasePresenter;
import com.havrylyuk.dou.utils.chart.listviewitems.ChartItem;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
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
                        .compose(getCompositeDisposableHelper().<List<ChartItem>>applySchedulers())
                        .subscribe(new Consumer<List<ChartItem>>() {
                            @Override
                            public void accept(List<ChartItem> chartItems) throws Exception {
                                if (chartItems.isEmpty()) {
                                    getMvpView().showEmptyView();
                                } else {
                                    getMvpView().showSalaries(chartItems);
                                }

                            }
                        }));
    }

}
