package com.havrylyuk.dou.ui.main;

import com.havrylyuk.dou.ui.base.Presenter;

/**
 * Created by Igor Havrylyuk on 31.09.2017.
 */

public interface MainMvpPresenter<V extends MainMvpView> extends Presenter<V> {

    void onDrawerAboutClick();

    void onDrawerSettingsClick();

    void onDrawerShareClick();

    void onNavMenuCreated();

    boolean isSync();

    void setSync(boolean isSync);
}
