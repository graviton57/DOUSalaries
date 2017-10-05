package com.havrylyuk.dou.data;

import android.content.Context;

import com.havrylyuk.dou.data.local.db.IDataBaseHelper;
import com.havrylyuk.dou.data.local.preferences.IPreferencesHelper;
import com.havrylyuk.dou.data.remote.IApiHelper;

/**
 * Created by Igor Havrylyuk on 13.09.2017.
 */

public interface IDataManager extends IPreferencesHelper, IDataBaseHelper, IApiHelper {

     Context  getContext();

}
