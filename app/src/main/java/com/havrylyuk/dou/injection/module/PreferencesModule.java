package com.havrylyuk.dou.injection.module;

import android.content.Context;

import com.havrylyuk.dou.data.local.preferences.AppPreferencesHelper;
import com.havrylyuk.dou.data.local.preferences.CommonPreferencesHelper;
import com.havrylyuk.dou.data.local.preferences.IPreferencesHelper;
import com.havrylyuk.dou.injection.qualifier.ApplicationContext;
import com.havrylyuk.dou.injection.scope.DouAppScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Igor Havrylyuk on 12.09.2017.
 */
@Module(includes = ContextModule.class)
public class PreferencesModule {

    @Provides
    @DouAppScope
    CommonPreferencesHelper getPreferencesHelper(
            @ApplicationContext Context context) {
        return new CommonPreferencesHelper(context);
    }

    @Provides
    @DouAppScope
    IPreferencesHelper getApplicationPreferences(
            CommonPreferencesHelper commonPreferencesHelper) {
        return new AppPreferencesHelper(commonPreferencesHelper);
    }

}
