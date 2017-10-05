package com.havrylyuk.dou.injection.module;

import com.havrylyuk.dou.data.remote.helper.CompositeDisposableHelper;
import com.havrylyuk.dou.utils.reactive.SchedulerProvider;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;


/**
 * Created by Igor Havrylyuk on 13.09.2017.
 */

@Module
public class RxModule {

    @Provides
    CompositeDisposable getCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    SchedulerProvider getSchedulerProvider() {
        return new SchedulerProvider();
    }

    @Provides
    CompositeDisposableHelper getCompositeDisposableHelper(
            CompositeDisposable compositeDisposable, SchedulerProvider schedulerProvider) {
        return new CompositeDisposableHelper(compositeDisposable, schedulerProvider);
    }

}
