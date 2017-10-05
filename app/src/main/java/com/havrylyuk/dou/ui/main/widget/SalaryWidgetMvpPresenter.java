package com.havrylyuk.dou.ui.main.widget;

import com.havrylyuk.dou.ui.base.Presenter;

/**
 * Created by Igor Havrylyuk on 23.09.2017.
 */

public interface SalaryWidgetMvpPresenter <V extends SalaryWidgetMvpView> extends Presenter<V> {

    void loadSalaries(long period, String language, String city,
                      String jobTitle, int experience);

}
