package com.havrylyuk.dou.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import com.havrylyuk.dou.injection.component.ActivityFragmentComponent;
import com.havrylyuk.dou.utils.events.SyncEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by Igor Havrylyuk on 30.08.2017.
 */

public abstract class BaseFragment extends Fragment implements BaseMvpView {

    private BaseActivity baseActivity;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            this.baseActivity = (BaseActivity) context;
        }
    }

    @Override
    public void onError(@StringRes int resId) {
        if (baseActivity != null) {
            baseActivity.onError(resId);
        }
    }

    @Override
    public void onError(String message) {
        if (baseActivity != null) {
            baseActivity.onError(message);
        }
    }

    @Override
    public boolean isNetworkConnected() {
        return baseActivity != null && baseActivity.isNetworkConnected();
    }

    @Override
    public void onDetach() {
        baseActivity = null;
        super.onDetach();
    }

    @Override
    public void hideKeyboard() {
        if (baseActivity != null) {
            baseActivity.hideKeyboard();
        }
    }

    public ActivityFragmentComponent getActivityFragmentComponent() {
        return baseActivity.getActivityFragmentComponent();
    }

    public BaseActivity getBaseActivity() {
        return baseActivity;
    }

    public void setUnBinder(Unbinder unBinder) {
        unbinder = unBinder;
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    public void showLoading() {
        if (baseActivity != null) {
            baseActivity.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (baseActivity != null) {
            baseActivity.hideLoading();
        }
    }

    protected abstract void initUI();

    protected abstract void updateData();

    interface BaseActivityCallback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }

    @Subscribe
    public void onEvent(SyncEvent event) {
        switch (event.getCode()) {
            case SyncEvent.UPDATE_SYNC:
                if (isAdded()){
                    updateData();
                    Timber.d("%s", event.getStatus());
                }
                break;
        }
    }
}
