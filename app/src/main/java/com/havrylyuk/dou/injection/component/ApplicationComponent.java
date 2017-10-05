package com.havrylyuk.dou.injection.component;

import android.content.Context;

import com.havrylyuk.dou.DouApp;
import com.havrylyuk.dou.data.IDataManager;
import com.havrylyuk.dou.injection.module.ApplicationModule;
import com.havrylyuk.dou.injection.qualifier.ApplicationContext;
import com.havrylyuk.dou.injection.scope.DouAppScope;
import com.havrylyuk.dou.data.SyncService;


import dagger.Component;

/**
 * Created by Igor Havrylyuk on 13.09.2017.
 */

@DouAppScope
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent  {

    void inject(DouApp application);

    void inject(SyncService service);

    @ApplicationContext
    Context context();

    IDataManager getDataManager();

}
