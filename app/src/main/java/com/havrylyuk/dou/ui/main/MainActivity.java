package com.havrylyuk.dou.ui.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.utils.Utils;
import com.havrylyuk.dou.BuildConfig;
import com.havrylyuk.dou.R;
import com.havrylyuk.dou.data.SyncService;
import com.havrylyuk.dou.ui.base.BaseActivity;
import com.havrylyuk.dou.utils.AppUtils;
import com.havrylyuk.dou.utils.events.SyncEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity
        implements MainMvpView, NavigationView.OnNavigationItemSelectedListener {

    private static final String IS_SYNC_KEY = "IS_SYNC_KEY";

    @Inject
    MainMvpPresenter<MainMvpView> presenter;

    @Inject
    SalariesPagerAdapter pagerAdapter;

    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.nav_drawer_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager_layout)
    SalariesViewPager viewPager;
    @BindView(R.id.tabs_layout)
    TabLayout tabLayout;
    @BindView(R.id.tv_app_version)
    TextView appVersion;
    @BindView(R.id.sync_progress)
    ProgressBar syncProgress;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActivityFragmentComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        setSupportActionBar(toolbar);
        Utils.init(this); // initialize the MPAndroidChart utilities
        presenter.attachView(this);
        initUI();
        if (savedInstanceState != null) {
            presenter.setSync(savedInstanceState.getBoolean(IS_SYNC_KEY));
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(IS_SYNC_KEY, presenter.isSync());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showLoading() {
        syncProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        syncProgress.setVisibility(View.GONE);
    }

    @Override
    public boolean isNetworkConnected() {
        return AppUtils.isNetworkAvailable(this);
    }

    @Override
    protected void initUI() {
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.menu_item_salaries));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        pagerAdapter = new SalariesPagerAdapter(getSupportFragmentManager());
        pagerAdapter.setPages(getResources().getStringArray(R.array.salaries_tab_titles));
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setPagingEnabled(false);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        presenter.onNavMenuCreated();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_sync:
                presenter.onDrawerSettingsClick();
                break;
            case R.id.nav_share:
                presenter.onDrawerShareClick();
                break;
            case R.id.nav_about:
                presenter.onDrawerAboutClick();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void showAboutFragment() {
        Toast.makeText(this,getString(R.string.about),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSettingActivity() {
        startService(new Intent(this, SyncService.class));
    }

    @Override
    public void updateAppVersion() {
        String version = getString(R.string.format_app_version, BuildConfig.VERSION_NAME);
        appVersion.setText(version);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            } else {
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            }
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name) +
                    "-> https://play.google.com/store/apps/details?id=" + getPackageName());
            startActivity(shareIntent);
        } catch (ActivityNotFoundException anf){
            //
        }
    }

    @Override
    public void closeNavigationDrawer() {
        if (drawer != null) {
            drawer.closeDrawer(Gravity.START);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(SyncEvent event) {
        switch (event.getCode()) {
            case SyncEvent.START_SYNC:
                showLoading();
                break;
            case SyncEvent.END_SYNC:
                hideLoading();
                EventBus.getDefault().post(new SyncEvent(SyncEvent.UPDATE_SYNC, ""));
                EventBus.getDefault().removeStickyEvent(event);
                break;
        }
        if(!event.getStatus().isEmpty()){
            Toast.makeText(this, event.getStatus(), Toast.LENGTH_SHORT).show();
        }
    }

}
