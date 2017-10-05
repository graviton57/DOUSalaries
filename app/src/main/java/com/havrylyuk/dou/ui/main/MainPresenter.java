package com.havrylyuk.dou.ui.main;

import com.havrylyuk.dou.data.IDataManager;
import com.havrylyuk.dou.data.remote.helper.CompositeDisposableHelper;
import com.havrylyuk.dou.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Igor Havrylyuk on 31.09.2017.
 */

public class MainPresenter<V extends MainMvpView> extends BasePresenter<V>
                                                  implements MainMvpPresenter<V> {

    private boolean isSync;

    @Inject
    public MainPresenter(CompositeDisposableHelper compositeDisposableHelper,
                         IDataManager dataManager) {
        super(compositeDisposableHelper, dataManager);
    }


    @Override
    public void onDrawerAboutClick() {
        getMvpView().closeNavigationDrawer();
        getMvpView().showAboutFragment();
    }

    @Override
    public void onDrawerSettingsClick() {
        getMvpView().closeNavigationDrawer();
        getMvpView().showSettingActivity();
    }

    @Override
    public void onDrawerShareClick() {
        getMvpView().closeNavigationDrawer();
        getMvpView().shareApp();
    }

    @Override
    public void onNavMenuCreated() {
        if (!isViewAttached()) {
            return;
        }
        getMvpView().updateAppVersion();
    }

    @Override
    public boolean isSync() {
        return isSync;
    }

    @Override
    public void setSync(boolean isSync) {
        this.isSync = isSync;
        if (isSync){
            getMvpView().showLoading();
        } else {
            getMvpView().hideLoading();
        }
    }
}
