package com.havrylyuk.dou.utils.reactive;

import android.support.annotation.NonNull;
import io.reactivex.Scheduler;

/**
 * Created by Igor Havrylyuk on 13.09.2017.
 */

public interface BaseSchedulerProvider {

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();

    @NonNull
    Scheduler computation();

}
