package com.havrylyuk.dou.injection.module;

import android.content.Context;

import com.havrylyuk.dou.injection.scope.DouAppScope;
import com.havrylyuk.dou.injection.qualifier.ApplicationContext;
import com.havrylyuk.dou.injection.scope.DouAppScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Igor Havrylyuk on 13.09.2017.
 */

@Module
public class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @DouAppScope
    @ApplicationContext
    public Context getApplicationContext() {
        return context;
    }
}
