package com.havrylyuk.dou;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.havrylyuk.dou.injection.component.ApplicationComponent;
import com.havrylyuk.dou.injection.component.DaggerApplicationComponent;
import com.havrylyuk.dou.injection.module.ApplicationModule;
import com.havrylyuk.dou.injection.module.ContextModule;
import com.havrylyuk.dou.injection.module.DatabaseModule;
import com.havrylyuk.dou.injection.module.NetworkModule;
import com.havrylyuk.dou.utils.NativeLibrariesManager;
import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Igor Havrylyuk on 13.09.2017.
 */

public class DouApp extends MultiDexApplication {

    @Inject
    CalligraphyConfig calligraphyConfig;

    public static ApplicationComponent applicationComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);//Init Fresco
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .contextModule(new ContextModule(this))
                .databaseModule(new DatabaseModule())
                .networkModule(new NetworkModule())
                .build();
        applicationComponent.inject(this);
        CalligraphyConfig.initDefault(calligraphyConfig);
        Timber.plant(new Timber.DebugTree());
        LeakCanary.install(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        loadNativeLibraries();
    }

    public static ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    protected void loadNativeLibraries() {
        NativeLibrariesManager.loadNativeLibraries();//load SQLiteX
    }
}
