package com.havrylyuk.dou.injection.module;

import android.app.Service;

import dagger.Module;

/**
 * Created by Igor Havrylyuk on 19.09.2017.
 */

@Module
public class ServiceModule {

    private final Service service;

    public ServiceModule(Service service) {
        this.service = service;
    }
}
