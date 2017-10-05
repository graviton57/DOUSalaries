package com.havrylyuk.dou.ui.main.demographics;

import com.havrylyuk.dou.ui.base.Presenter;

/**
 * Created by Igor Havrylyuk on 25.09.2017.
 */

public interface DemographicMvpPresenter<V extends DemographicMvpView> extends Presenter<V> {

    void loadDemographic(long period);

}
