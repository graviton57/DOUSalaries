package com.havrylyuk.dou.injection.module;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.havrylyuk.dou.injection.qualifier.ActivityContext;
import com.havrylyuk.dou.injection.scope.ActivityScope;
import com.havrylyuk.dou.ui.main.MainMvpPresenter;
import com.havrylyuk.dou.ui.main.MainMvpView;
import com.havrylyuk.dou.ui.main.MainPresenter;
import com.havrylyuk.dou.ui.main.SalariesAdapter;
import com.havrylyuk.dou.ui.main.SalariesPagerAdapter;
import com.havrylyuk.dou.ui.main.by_cities.ByCitiesMvpPresenter;
import com.havrylyuk.dou.ui.main.by_cities.ByCitiesMvpView;
import com.havrylyuk.dou.ui.main.by_cities.ByCitiesPresenter;
import com.havrylyuk.dou.ui.main.by_years.ByYearsMvpPresenter;
import com.havrylyuk.dou.ui.main.by_years.ByYearsMvpView;
import com.havrylyuk.dou.ui.main.by_years.ByYearsPresenter;
import com.havrylyuk.dou.ui.main.demographics.DemographicMvpPresenter;
import com.havrylyuk.dou.ui.main.demographics.DemographicMvpView;
import com.havrylyuk.dou.ui.main.demographics.DemographicPresenter;
import com.havrylyuk.dou.ui.main.widget.SalaryWidgetMvpPresenter;
import com.havrylyuk.dou.ui.main.widget.SalaryWidgetMvpView;
import com.havrylyuk.dou.ui.main.widget.SalaryWidgetPresenter;
import com.havrylyuk.dou.ui.splash.SplashMvpPresenter;
import com.havrylyuk.dou.ui.splash.SplashMvpView;
import com.havrylyuk.dou.ui.splash.SplashPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Igor Havrylyuk on 20.05.2017.
 */

@Module(includes = RxModule.class)
public class ActivityFragmentModule {

    private Activity activity;

    public ActivityFragmentModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    Activity getActivity() {
        return activity;
    }

    @Provides
    @ActivityContext
    Context getActivityContext() {
        return activity;
    }

    //Activities
    @Provides
    @ActivityScope
    MainMvpPresenter<MainMvpView> getMainPresenter(
            MainPresenter<MainMvpView> presenter) {
        return presenter;
    }

    @Provides
    @ActivityScope
    SplashMvpPresenter<SplashMvpView> getSplashPresenter(
            SplashPresenter<SplashMvpView> presenter) {
        return presenter;
    }

    //Fragments

    @Provides
    @ActivityScope
    SalaryWidgetMvpPresenter<SalaryWidgetMvpView> getSalaryWidgetPresenter (
            SalaryWidgetPresenter<SalaryWidgetMvpView> presenter) {
        return presenter;
    }

    @Provides
    @ActivityScope
    ByCitiesMvpPresenter<ByCitiesMvpView> getSalaryByCitiesPresenter(
            ByCitiesPresenter<ByCitiesMvpView> presenter ){
        return presenter;
    }

    @Provides
    @ActivityScope
    ByYearsMvpPresenter<ByYearsMvpView> getByYearsMvpPresenter(
            ByYearsPresenter<ByYearsMvpView> presenter){
        return presenter;
    }

    @Provides
    @ActivityScope
    DemographicMvpPresenter<DemographicMvpView> getDemographicMvpPresenter(
            DemographicPresenter<DemographicMvpView> presenter){
        return presenter;
    }

    // Adapters

    @Provides
    @ActivityScope
    SalariesPagerAdapter getSalariesPagerAdapter(@ActivityContext Context context) {
        return new SalariesPagerAdapter(((AppCompatActivity)context).getSupportFragmentManager());
    }

    @Provides
    @ActivityScope
    SalariesAdapter getSalariesAdapter(@ActivityContext Context context) {
        return new SalariesAdapter(context);
    }

    // Services

}
