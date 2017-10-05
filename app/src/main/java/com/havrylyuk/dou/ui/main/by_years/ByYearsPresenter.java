package com.havrylyuk.dou.ui.main.by_years;

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
