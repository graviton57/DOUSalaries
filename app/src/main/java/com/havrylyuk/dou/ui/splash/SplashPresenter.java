package com.havrylyuk.dou.ui.splash;

import com.havrylyuk.dou.data.IDataManager;
import com.havrylyuk.dou.data.remote.helper.CompositeDisposableHelper;
import com.havrylyuk.dou.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Igor Havrylyuk on 29.09.2017.
 */

public class SplashPresenter <V extends SplashMvpView> extends BasePresenter<V>
        implements SplashMvpPresenter<V> {

    @Inject
    public SplashPresenter(CompositeDisposableHelper compositeDisposableHelper, IDataManager dataManager) {
        super(compositeDisposableHelper, dataManager);
    }

}
