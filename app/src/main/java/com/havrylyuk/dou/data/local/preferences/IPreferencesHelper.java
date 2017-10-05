package com.havrylyuk.dou.data.local.preferences;

/**
 * Created by Igor Havrylyuk on 10.09.2017.
 */

public interface IPreferencesHelper {

    void setLoggedIn();

    boolean isLoggedIn();

    void setUserName(String userName);

    String getUserName();

    Boolean isPublicIcons();

    void setPublicIcons(boolean isPublicDomain);

}
