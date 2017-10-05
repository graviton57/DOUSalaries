package com.havrylyuk.dou.injection.module;

import android.app.Application;
import android.content.Context;

import com.havrylyuk.dou.R;
import com.havrylyuk.dou.data.DataManager;
import com.havrylyuk.dou.data.IDataManager;
import com.havrylyuk.dou.data.local.db.IDataBaseHelper;
import com.havrylyuk.dou.data.local.preferences.IPreferencesHelper;
import com.havrylyuk.dou.data.remote.IApiHelper;
import com.havrylyuk.dou.injection.qualifier.ApplicationContext;
import com.havrylyuk.dou.injection.scope.DouAppScope;

import dagger.Module;
import dagger.Provides;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Igor Havrylyuk on 30.08.2017.
 */

@Module(includes = {
    ContextModule.class, PreferencesModule.class, NetworkModule.class, DatabaseModule.class
})
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Application getApplication() {
        return application;
    }

    @Provides
    @DouAppScope
    CalligraphyConfig getCalligraphyConfig() {
        return new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build();
    }

    @Provides
    @DouAppScope
    DataManager getAppDataManager(@ApplicationContext Context context,
                                  IPreferencesHelper preferencesHelper, IDataBaseHelper dbHelper,
                                  IApiHelper apiHelper) {
        return new DataManager(context, dbHelper, preferencesHelper, apiHelper);
    }

    @Provides
    @DouAppScope
    IDataManager getDataManager(DataManager appDataManager) {
        return appDataManager;
    }

}


