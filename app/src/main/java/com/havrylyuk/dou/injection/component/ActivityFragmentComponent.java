package com.havrylyuk.dou.injection.component;

import com.havrylyuk.dou.injection.module.ActivityFragmentModule;
import com.havrylyuk.dou.injection.scope.ActivityScope;
import com.havrylyuk.dou.ui.main.MainActivity;
import com.havrylyuk.dou.ui.main.by_cities.ByCitiesFragment;
import com.havrylyuk.dou.ui.main.by_years.ByYearsFragment;
import com.havrylyuk.dou.ui.main.demographics.DemographicFragment;
import com.havrylyuk.dou.ui.main.widget.SalaryWidgetFragment;
import com.havrylyuk.dou.ui.splash.SplashActivity;

import dagger.Component;

/**
 * Created by Igor Havrylyuk on 13.09.2017.
 */

@ActivityScope
@Component(dependencies = ApplicationComponent.class,
           modules = ActivityFragmentModule.class)
public interface ActivityFragmentComponent {

    void inject(SplashActivity splashActivity);

    void inject(MainActivity mainActivity);

    void inject(SalaryWidgetFragment salaryWidgetFragment);

    void inject(ByCitiesFragment byCitiesFragment);

    void inject(ByYearsFragment byYearsFragment);

    void inject(DemographicFragment demographicFragment);

}
