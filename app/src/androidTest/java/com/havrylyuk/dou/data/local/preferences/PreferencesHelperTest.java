package com.havrylyuk.dou.data.local.preferences;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.havrylyuk.dou.data.local.preferences.AppPreferencesHelper;
import com.havrylyuk.dou.data.local.preferences.CommonPreferencesHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Igor Havrylyuk on 21.09.2017.
 */
@RunWith(AndroidJUnit4.class)
public class PreferencesHelperTest {

    private AppPreferencesHelper appPreferencesHelper;

    @Before
    public void setUp(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        CommonPreferencesHelper commonPreferencesHelper = new CommonPreferencesHelper(
                appContext);
        appPreferencesHelper = new AppPreferencesHelper(commonPreferencesHelper);
    }

    @Test
    public void putAndGetUserName(){
        String userName = "Igor";
        appPreferencesHelper.setUserName(userName);
        assertEquals(userName, appPreferencesHelper.getUserName());
    }

    @Test
    public void putAndGetLoginInfo(){
        appPreferencesHelper.setLoggedIn();
        assertTrue(appPreferencesHelper.isLoggedIn());
    }

    @Test
    public void putAndGetPublicIconInfo(){
        appPreferencesHelper.setPublicIcons(true);
        assertTrue(appPreferencesHelper.isPublicIcons());
    }
}
