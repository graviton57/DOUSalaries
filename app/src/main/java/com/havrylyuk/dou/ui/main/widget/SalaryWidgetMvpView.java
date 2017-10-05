package com.havrylyuk.dou.ui.main.widget;

import com.havrylyuk.dou.data.local.model.SalaryDataForWidget;
import com.havrylyuk.dou.ui.base.BaseMvpView;

/**
 * Created by Igor Havrylyuk on 23.09.2017.
 */

public interface SalaryWidgetMvpView  extends BaseMvpView {

    void showSalaries(SalaryDataForWidget salaryDataForWidget);

    void showEmptyView();
}
