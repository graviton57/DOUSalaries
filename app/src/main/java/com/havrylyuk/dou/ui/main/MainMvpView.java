package com.havrylyuk.dou.ui.main;



import com.havrylyuk.dou.ui.base.BaseMvpView;


/**
 * Created by Igor Havrylyuk on 31.08.2017.
 */

public interface MainMvpView extends BaseMvpView {

    void showAboutFragment();

    void showSettingActivity();

    void shareApp();

    void closeNavigationDrawer();

    void updateAppVersion();

}
