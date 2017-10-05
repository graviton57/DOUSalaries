package com.havrylyuk.dou.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.havrylyuk.dou.DouApp;
import com.havrylyuk.dou.injection.component.ActivityFragmentComponent;
import com.havrylyuk.dou.injection.component.DaggerActivityFragmentComponent;
import com.havrylyuk.dou.injection.module.ActivityFragmentModule;
import com.havrylyuk.dou.utils.AppUtils;

import butterknife.Unbinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Igor Havrylyuk on 13.09.2017.
 */

public abstract class BaseActivity  extends AppCompatActivity
        implements BaseMvpView, BaseFragment.BaseActivityCallback {

    private ActivityFragmentComponent activityFragmentComponent;
    private Unbinder unbinder;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFragmentComponent = DaggerActivityFragmentComponent.builder()
                .activityFragmentModule(new ActivityFragmentModule(this))
                .applicationComponent(DouApp.getApplicationComponent())
                .build();
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    public ActivityFragmentComponent getActivityFragmentComponent() {
        return activityFragmentComponent;
    }

    @Override
    public void onError(@StringRes int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean isNetworkConnected() {
        return AppUtils.isNetworkAvailable(getApplicationContext());
    }

    @Override
    public void hideKeyboard() {
        AppUtils.hideKeyboard(this);
    }

    public void setUnBinder(Unbinder unBinder) {
        unbinder = unBinder;
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    protected abstract void initUI();

  }
