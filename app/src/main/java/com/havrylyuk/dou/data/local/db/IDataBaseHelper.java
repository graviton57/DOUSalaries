package com.havrylyuk.dou.data.local.db;

import android.content.ContentValues;

import com.havrylyuk.dou.data.local.model.SalaryDataForWidget;
import com.havrylyuk.dou.utils.chart.listviewitems.ChartItem;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Igor Havrylyuk on 13.09.2017.
 */

public interface IDataBaseHelper {

    Long saveSalaries(ContentValues[] values);

    int deleteAllSalaries();

    Observable<SalaryDataForWidget> getSalaryForWidget(long period, String language, String city,
                                                       String jobTitle, int experience);

    Observable<List<ChartItem>> getSalaryForCities(long period);

    Observable<List<ChartItem>> getSalaryForYears(String city);

    Observable<List<ChartItem>> getSalaryForDemographic(long period);
}
