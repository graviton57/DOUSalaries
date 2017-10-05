package com.havrylyuk.dou.injection.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Igor Havrylyuk on 13.09.2017.
 */

@Scope
@Retention(RetentionPolicy.CLASS)
public @interface DouAppScope {
}
