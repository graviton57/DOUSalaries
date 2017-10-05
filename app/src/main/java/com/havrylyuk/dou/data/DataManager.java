package com.havrylyuk.dou.data;

import android.content.ContentValues;
import android.content.Context;

import com.havrylyuk.dou.data.local.db.IDataBaseHelper;
import com.havrylyuk.dou.data.local.model.SalaryDataForWidget;
import com.havrylyuk.dou.data.local.preferences.IPreferencesHelper;
import com.havrylyuk.dou.data.remote.IApiHelper;
import com.havrylyuk.dou.data.remote.helper.error.ErrorHandlerHelper;
import com.havrylyuk.dou.utils.chart.listviewitems.ChartItem;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * App Data Manager class
 * Created by Igor Havrylyuk on 13.09.2017.
 */

public class DataManager implements IDataManager {

    private final Context context;
    private final IDataBaseHelper dbHelper;
    private final IPreferencesHelper applicationPreferences;
    private final IApiHelper apiHelper;

    @Inject
    public DataManager(Context context, IDataBaseHelper dbHelper,
                       IPreferencesHelper applicationPreferences, IApiHelper apiHelper) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.applicationPreferences = applicationPreferences;
        this.apiHelper = apiHelper;
    }

    @Override
    public Context getContext() {
        return context;
    }

    //SharedPreference Helper methods
    @Override
    public void setLoggedIn() {
        applicationPreferences.setLoggedIn();
    }

    @Override
    public boolean isLoggedIn() {
        return applicationPreferences.isLoggedIn();
    }

    @Override
    public void setUserName(String userName) {
        applicationPreferences.setUserName(userName);
    }

    @Override
    public String getUserName() {
        return applicationPreferences.getUserName();
    }

    @Override
    public Boolean isPublicIcons() {
        return applicationPreferences.isPublicIcons();
    }

    @Override
    public void setPublicIcons(boolean isPublicDomain) {
        applicationPreferences.setPublicIcons(isPublicDomain);
    }

    //Database Helper methods

    @Override
    public Long saveSalaries(ContentValues[] values) {
        return dbHelper.saveSalaries(values);
    }

    @Override
    public int deleteAllSalaries() {
        return dbHelper.deleteAllSalaries();
    }

    @Override
    public Observable<SalaryDataForWidget> getSalaryForWidget(long period, String language, String city,
                                                              String jobTitle, int experience) {
        return dbHelper.getSalaryForWidget(period, language, city, jobTitle, experience);
    }

    @Override
    public Observable<List<ChartItem>> getSalaryForCities(long period) {
        return dbHelper.getSalaryForCities(period);
    }

    @Override
    public Observable<List<ChartItem>> getSalaryForYears(String city) {
        return dbHelper.getSalaryForYears(city);
    }

    @Override
    public Observable<List<ChartItem>> getSalaryForDemographic(long period) {
        return dbHelper.getSalaryForDemographic(period);
    }

    //Api Helper methods

    @Override
    public Call<ResponseBody> downloadDouFile(String fileUrl) {
        return apiHelper.downloadDouFile(fileUrl);
    }

    @Override
    public ErrorHandlerHelper getErrorHandlerHelper() {
        return apiHelper.getErrorHandlerHelper();
    }

    @Override
    public void setErrorHandler(ErrorHandlerHelper errorHandler) {
        apiHelper.setErrorHandler(errorHandler);
    }
}
