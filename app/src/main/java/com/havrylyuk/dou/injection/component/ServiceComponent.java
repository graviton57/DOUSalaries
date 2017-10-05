package com.havrylyuk.dou.injection.component;

import com.havrylyuk.dou.injection.module.ServiceModule;
import com.havrylyuk.dou.injection.scope.PerService;
import com.havrylyuk.dou.data.SyncService;

import dagger.Component;

/**
 * Created by Igor Havrylyuk on 19.09.2017.
 */

@PerService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {

    void inject(SyncService service);

}
