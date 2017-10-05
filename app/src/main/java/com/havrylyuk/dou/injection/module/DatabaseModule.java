package com.havrylyuk.dou.injection.module;

import android.content.Context;

import com.havrylyuk.dou.Config;
import com.havrylyuk.dou.data.local.db.DataBaseHelper;
import com.havrylyuk.dou.data.local.db.DouDbHelper;
import com.havrylyuk.dou.injection.scope.DouAppScope;
import com.havrylyuk.dou.data.local.db.IDataBaseHelper;
import com.havrylyuk.dou.injection.qualifier.ApplicationContext;

import java.io.File;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Igor Havrylyuk on 30.08.2017.
 */

@Module(includes = ContextModule.class)
public class DatabaseModule {

    @Provides
    @DouAppScope
    public DouDbHelper douDbHelper(@ApplicationContext Context context) {
        File f = context.getDatabasePath(DouDbHelper.DATABASE_NAME);
        f.getParentFile().mkdirs();
        DouDbHelper helper = new DouDbHelper(context, f.getPath());
        helper.getReadableDatabase().loadExtension(Config.EXTENSIONS_LIB);
        return helper;
    }

    @Provides
    @DouAppScope
    public IDataBaseHelper dbHelper(DouDbHelper dbHelper) {
        return new DataBaseHelper(dbHelper);
    }

}
