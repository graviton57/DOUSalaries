package com.havrylyuk.dou.data.local.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.havrylyuk.dou.BuildConfig;
import com.havrylyuk.dou.Config;
import com.havrylyuk.dou.R;
import com.havrylyuk.dou.data.local.db.DouContract.SalaryEntry;
import com.havrylyuk.dou.data.local.model.SalaryData;
import com.havrylyuk.dou.data.local.model.SalaryDataForWidget;
import com.havrylyuk.dou.ui.main.demographics.DemographicsType;
import com.havrylyuk.dou.utils.AppUtils;
import com.havrylyuk.dou.utils.SQLUtils;
import com.havrylyuk.dou.utils.chart.ChartHelper;
import com.havrylyuk.dou.utils.chart.listviewitems.ChartItem;
import com.havrylyuk.dou.utils.chart.listviewitems.GroupBarChartItem;
import com.havrylyuk.dou.utils.chart.listviewitems.HorizontalBarChartItem;
import com.havrylyuk.dou.utils.chart.listviewitems.LineChartItem;
import com.havrylyuk.dou.utils.chart.listviewitems.PieChartItem;
import com.havrylyuk.dou.utils.chart.listviewitems.VerticalBarChartItem;
import com.havrylyuk.dou.utils.chart.marker.EntryMarkerData;

import org.joda.time.DateTime;
import org.sqlite.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import timber.log.Timber;

import static com.github.mikephil.charting.utils.ColorTemplate.VORDIPLOM_COLORS;
import static dagger.internal.Preconditions.checkNotNull;

/**
 * DataBase Helper class
 * Created by Igor Havrylyuk on 14.09.2017.
 */

@Singleton
public class DataBaseHelper implements IDataBaseHelper {

    private DouDbHelper douDbHelper;
    private Context context;
    private final SQLiteDatabase db;

    @Inject
    public DataBaseHelper(DouDbHelper douDbHelper) {
        Timber.d("Create DataBaseHelper");
        this.douDbHelper = douDbHelper;
        this.context = douDbHelper.getContext();
        db = douDbHelper.getReadableDatabase();
        db.loadExtension(Config.EXTENSIONS_LIB);
    }

    @Override
    public Long saveSalaries(ContentValues[] values) {
        long returnCount = 0L;
        final SQLiteDatabase db = douDbHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            for (ContentValues value : values) {
                checkNotNull(value);
                long _id = db.insert(SalaryEntry.TABLE_NAME, null, value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        }
        catch(Exception e){
            returnCount = -1L;
        }
        finally {
            db.endTransaction();
        }
        //logTable(ArticlesEntry.TABLE_NAME);
        return returnCount;
    }

    @Override
    public int deleteAllSalaries() {
        final SQLiteDatabase db = douDbHelper.getWritableDatabase();
        return db.delete(SalaryEntry.TABLE_NAME, null, null);
    }

    @Override
    public Observable<SalaryDataForWidget> getSalaryForWidget(final long period, final String language,
                                                              final String city, final String jobTitle,
                                                              final int experience) {
        return Observable.fromCallable(() -> {
            SalaryDataForWidget salary = new SalaryDataForWidget();
            Cursor cursor = db.query(SalaryEntry.TABLE_NAME,
                    SQLUtils.columnsForWidget,
                    SQLUtils.buildSelectionsForWidget(context, city, language),
                    SQLUtils.buildSelectionsForWidgetArgs(context, String.valueOf(period),
                            language, city, jobTitle, experience),
                    null, null,
                    SalaryEntry.SALARIES_PER_MONTH);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int q1 = (int) cursor.getDouble(0);
                    int median = (int) cursor.getDouble(1);
                    int q3 = (int) cursor.getDouble(2);
                    int count = (int) cursor.getDouble(3);
                    Timber.d("SalaryData q1=%d , median=%d , q3=%d , count=%d", q1, median, q3, count);
                    salary.setQ1(q1);
                    salary.setMedian(median);
                    salary.setQ3(q3);
                    salary.setSalariesCount(count);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return salary;
        });
    }

    @Override
    public Observable<List<ChartItem>> getSalaryForCities(final long period) {
        return  Observable.fromCallable(() -> {
            Cursor cursor = db.query(SalaryEntry.TABLE_NAME,
                    SQLUtils.columnsForCitiesAndYears,
                    SQLUtils.buildSelectionsWithYear(context),
                    SQLUtils.buildSelectionsArgsWithYar(String.valueOf(period)),
                    SalaryEntry.SALARIES_PROG_LANG + " , " + SalaryEntry.SALARIES_JOB_TITLE +
                            " , " + SalaryEntry.SALARIES_CITY,
                    " COUNT(*) > " + Config.MIN_RESULT_COUNT,
                    SalaryEntry.SALARIES_MEDIAN + " DESC "); //junior < engineer < senior
            List<ChartItem> res = buildChartItem(cursor);
            cursor.close();
            return res;
        });
    }

    @Override
    public Observable<List<ChartItem>> getSalaryForYears(final String city) {
        return  Observable.fromCallable(() -> {
            Cursor cursor = db.query(SalaryEntry.TABLE_NAME,
                    SQLUtils.columnsForCitiesAndYears,
                    SQLUtils.buildSelectionsWithCity(context, city),
                    SQLUtils.buildSelectionsArgsWithCity(context, city),
                    SalaryEntry.SALARIES_PERIOD + " , " + SalaryEntry.SALARIES_JOB_TITLE +
                            " , " + SalaryEntry.SALARIES_PROG_LANG,
                    " COUNT(*) > " + Config.MIN_RESULT_COUNT,
                    SalaryEntry.SALARIES_PERIOD);
            List<ChartItem> res = buildLineChartItem(cursor);
            cursor.close();
            return res;
        });
    }

    @Override
    public Observable<List<ChartItem>> getSalaryForDemographic(final long period) {
        return  Observable.fromCallable(() -> {
            List<ChartItem> res = new ArrayList<>();
            addChartItem(res, buildAgeChartItem(db, period));//age
            addChartItem(res, buildJobsPopularityChartItem(db, period));//jobs popularity
            addChartItem(res, buildExperienceChartItem(db, period));//experience
            addChartItem(res, buildPieChartItem(db, period,
                    DemographicsType.GENDER, DemographicsType.GENDER.getName())); //gender
            addChartItem(res, buildPieChartItem(db, period,
                    DemographicsType.ENG_LEVEL, DemographicsType.ENG_LEVEL.getName()));//english level
            addChartItem(res, buildCityChartItem(db, period));//city
            addChartItem(res, buildPieChartItem(db, period,//education
                    DemographicsType.EDUCATION, DemographicsType.EDUCATION.getName()));//companySize
            addChartItem(res, buildPieChartItem(db, period,
                    DemographicsType.COMPANY_SIZE, DemographicsType.COMPANY_SIZE.getName()));
            addChartItem(res, buildPieChartItem(db, period,
                    DemographicsType.COMPANY_TYPE, DemographicsType.COMPANY_TYPE.getName())); //companyType
            return res;
        });
    }

    /** do not add chart item without data*/
    private void addChartItem(List<ChartItem> items, ChartItem item){
        if (item != null) {
            items.add(item);
        }
    }

    /**Pies chart items*/

    private ChartItem buildPieChartItem(SQLiteDatabase db, long period,
                                        DemographicsType type, String title) {
        ChartItem result = null;
        Cursor cursor = buildCursorForCharts(type, db, period);
        try {
            if (cursor == null) {
                Timber.e("Error querying %s (got null cursor)", type.getName());
                return null;
            }
            if (cursor.getCount() < 1) {
                Timber.e("Error querying %s (no records returned)", type.getName());
                return null;
            }
            List<PieEntry> entries = new ArrayList<>();
            while (cursor.moveToNext()) {
                entries.add(new PieEntry( cursor.getFloat(1), cursor.getString(0)));
            }
            PieData chartData = ChartHelper.populatePieData(entries, "");
            result = new PieChartItem(context, chartData, title);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    private ChartItem buildCityChartItem(SQLiteDatabase db, long period) {
        ChartItem result = null;
        Cursor cursor = buildCursorForCharts(DemographicsType.CITY, db, period);
        if (cursor != null) {
            List<PieEntry> entries = new ArrayList<>();
            while (cursor.moveToNext()) {
                if (cursor.getInt(1) > Config.SALARIES_IN_CITY) {
                    entries.add(new PieEntry( cursor.getInt(1), cursor.getString(0)));
                }
            }
            if (!entries.isEmpty()){
                PieData chartData = ChartHelper.populatePieData(entries, "");
                result = new PieChartItem(context, chartData, context.getString(R.string.pie_city));
            }
            cursor.close();
        }
        return result;
    }

    /**BarData chart items*/

    private ChartItem buildAgeChartItem(SQLiteDatabase db, long period) {
        ChartItem result = null;
        Cursor cursor = buildCursorForCharts(DemographicsType.AGE, db, period);
        if (cursor != null) {
            List<BarEntry> entries = new ArrayList<>();
            while (cursor.moveToNext()) {
                if (cursor.getInt(0) > 0){// age>0
                    float value = cursor.getFloat(0);
                    int count = cursor.getInt(1);
                    entries.add(new BarEntry(value, count, new EntryMarkerData(value, count)));
                }
            }
            if (!entries.isEmpty()){
                BarData barData = ChartHelper.populateBarData(entries, "", null);
                result = new VerticalBarChartItem(context, barData);
                ((VerticalBarChartItem) result).setTitle(context.getString(R.string.chart_item_age));
            }
            cursor.close();
        }
        return result;
    }

    private ChartItem buildExperienceChartItem(SQLiteDatabase db, long period) {
        ChartItem result = null;
        Cursor cursor = buildCursorForCharts(DemographicsType.EXPERIENCE, db, period);
        try {
            if (cursor == null) {
                Timber.e("Error querying Experience (got null cursor)");
                return null;
            }
            if (cursor.getCount() < 1) {
                Timber.e("Error querying Experience(no records returned)");
                return null;
            }
            List<BarEntry> entries = new ArrayList<>();
            while (cursor.moveToNext()) {
                float value = cursor.getFloat(0);
                int count = cursor.getInt(1);
                entries.add(new BarEntry(value, count, new EntryMarkerData(value, count)));
            }
            BarData barData = ChartHelper.populateBarData(entries,
                    context.getString(R.string.chart_item_experience), ChartHelper.populateColors());
            result = new VerticalBarChartItem(context, barData);
            ((VerticalBarChartItem) result).setTitle(context.getString(R.string.chart_item_experience));

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    private ChartItem buildJobsPopularityChartItem(SQLiteDatabase db, long period) {
        ChartItem result = null;
        Cursor cursor = buildCursorForCharts(DemographicsType.JOB_TITLE, db, period);
        if (cursor != null) {
            ArrayList<BarEntry> entries = new ArrayList<>();
            while (cursor.moveToNext()) {
                String jobTitle = cursor.getString(0);
                int count = cursor.getInt(1);
                int jobIndex = AppUtils.getJobTitleIndex(context, jobTitle);
                Timber.d("add job title=%s, jobIndex=%d, count=%d", jobTitle, jobIndex, count);
                entries.add(new BarEntry(jobIndex, count, jobTitle));
            }
            if (!entries.isEmpty()){
                BarDataSet d = new BarDataSet(entries, "" );
                result = new HorizontalBarChartItem(context, new BarData(d));
                ((HorizontalBarChartItem) result)
                        .setTitle(context.getString(R.string.chart_item_job_titles));
            }
            cursor.close();
        }
        return result;
    }

    /**GroupBarData chart items*/

    private List<ChartItem> buildChartItem(Cursor cursor){
        ArrayList<ChartItem> entries = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            for (String language : Config.LANGUAGES) {
                List<BarEntry> yKiev    = new ArrayList<>();
                List<BarEntry> yLviv    = new ArrayList<>();
                List<BarEntry> yKharkiv = new ArrayList<>();
                List<BarEntry> yDnipro  = new ArrayList<>();
                List<BarEntry> yOdessa  = new ArrayList<>();
                for (int i = 0; i < Config.JOB_SOFT.length; i++) {
                    while (cursor.moveToNext()) {
                        if (cursor.getString(1).equals(language)) {//language = java
                            String period = new DateTime(cursor.getLong(4))
                                    .toString(Config.CHART_DATE_FORMAT, Locale.US);
                            int salariesCount = (int) cursor.getFloat(3);
                            String city = cursor.getString(5);
                            String jobTitle = cursor.getString(0);
                            float salaryValue = cursor.getFloat(2);
                            SalaryData salary = new SalaryData(period, salariesCount, jobTitle);
                            if (city.equals(SQLUtils.CITIES[0])) {//Kiev
                                yKiev.add(new BarEntry(i, salaryValue, salary));
                            }
                            if (city.equals(SQLUtils.CITIES[1])) {//Lviv
                                yLviv.add(new BarEntry(i, salaryValue, salary));
                            }
                            if (city.equals(SQLUtils.CITIES[2])) {//Kharkiv
                                yKharkiv.add(new BarEntry(i,salaryValue, salary));
                            }
                            if (city.equals(SQLUtils.CITIES[3])) {//Dnipro
                                yDnipro.add(new BarEntry(i, salaryValue, salary));
                            }
                            if (city.equals(SQLUtils.CITIES[4])) {//Odessa
                                yOdessa.add(new BarEntry(i, salaryValue, salary));
                            }
                        }
                    }
                    cursor.moveToFirst();
                }// end for job titles
                BarDataSet set1, set2, set3, set4, set5;
                // create 4 DataSets
                set1 = new BarDataSet(yKiev, context.getString(R.string.city_kiev));
                set1.setColor(VORDIPLOM_COLORS[0]);
                set2 = new BarDataSet(yLviv, context.getString(R.string.city_lviv));
                set2.setColor(VORDIPLOM_COLORS[1]);
                set3 = new BarDataSet(yKharkiv, context.getString(R.string.city_kharkiv));
                set3.setColor(VORDIPLOM_COLORS[2]);
                set4 = new BarDataSet(yDnipro, context.getString(R.string.city_dnipro));
                set4.setColor(VORDIPLOM_COLORS[3]);
                set5 = new BarDataSet(yOdessa, context.getString(R.string.city_odessa));
                set5.setColor(VORDIPLOM_COLORS[4]);
                BarData data = new BarData(set1, set2, set3, set4, set5);
                data.setValueFormatter(new LargeValueFormatter());
                GroupBarChartItem chartItem = new GroupBarChartItem(context, data);
                chartItem.setLanguage(language);
                entries.add(chartItem);
            }
        }
        return entries;
    }

    //Return cursor
    private Cursor buildCursorForCharts(DemographicsType type, SQLiteDatabase db, long period){
        return  db.query(SalaryEntry.TABLE_NAME,
                SQLUtils.buildProjectionForDemographics(type),
                SalaryEntry.SALARIES_PERIOD + " =? ",
                new String[]{String.valueOf(period)},
                SQLUtils.buildGroupByForDemographics(type),
                null,
                SQLUtils.buildSortOrderForDemographics(type));
    }

    private List<ChartItem> buildLineChartItem(Cursor cursor) {
        ArrayList<ChartItem> entries = new ArrayList<>();
        LineChartItem chartItem;
        if (cursor != null && cursor.moveToFirst()) {
            for (String language : context.getResources().getStringArray(R.array.prog_lang)) {
                chartItem = populateDataForLanguage(language, cursor);
                if (chartItem != null) {
                    entries.add(chartItem);
                }
            }
            chartItem = populateDataForLanguage(Config.MANAGER, cursor);
            if ( chartItem != null) {
                entries.add(chartItem);
            }
        }
        return entries;
    }

    private String convertPeriod(long dateTime){
        return new DateTime(dateTime).toString(Config.CHART_DATE_FORMAT, Locale.US);
    }

    private LineChartItem populateDataForLanguage(@NonNull String language, Cursor cursor ){
        LineData lineData;
        List<Entry> seniors = new ArrayList<>();
        List<Entry> engineer = new ArrayList<>();
        List<Entry> juniors = new ArrayList<>();
        cursor.moveToFirst();//set cursor up
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            String period = new DateTime(cursor.getLong(4))
                    .toString(Config.CHART_DATE_FORMAT, Locale.US);
            if (language.equals(cursor.getString(1))){
                if (Config.JOB_SOFT[0].equals(cursor.getString(0))){//senior software engineer
                    seniors.add(new Entry(AppUtils.getPeriodIndex(context, period),
                            (int) cursor.getFloat(2),
                            new SalaryData(period, (int) cursor.getFloat(3))));
                }
                if (Config.JOB_SOFT[1].equals(cursor.getString(0))){//software engineer
                    engineer.add(new Entry(AppUtils.getPeriodIndex(context, period),
                            (int) cursor.getFloat(2),
                            new SalaryData(period,(int) cursor.getFloat(3))));
                }
                if (Config.JOB_SOFT[2].equals(cursor.getString(0))){//juniors software engineer
                    juniors.add(new Entry(AppUtils.getPeriodIndex(context,period),
                            (int) cursor.getFloat(2),
                            new SalaryData(period,(int) cursor.getFloat(3))));
                }
                // QA
                if (language.isEmpty() ){
                    if (Config.JOB_QA[0].equals(cursor.getString(0))) {//senior QA
                        seniors.add(new Entry(AppUtils.getPeriodIndex(context, period),
                                (int) cursor.getFloat(2),
                                new SalaryData(period, (int) cursor.getFloat(3))));
                    }
                    if (Config.JOB_QA[1].equals(cursor.getString(0))) {//engineer QA
                        engineer.add(new Entry(AppUtils.getPeriodIndex(context, period),
                                (int) cursor.getFloat(2),
                                new SalaryData(period, (int) cursor.getFloat(3))));
                    }
                    if (Config.JOB_QA[2].equals(cursor.getString(0))) {//juniors QA
                        juniors.add(new Entry(AppUtils.getPeriodIndex(context, period),
                                (int) cursor.getFloat(2),
                                new SalaryData(period, (int) cursor.getFloat(3))));
                    }
                }
            }
            if (language.equals(Config.MANAGER)) { //Management
                if (Config.JOB_MANAGER[0].equals(cursor.getString(0))) {//Senior Project Manager/PM
                    seniors.add(new Entry(AppUtils.getPeriodIndex(context, period),
                            (int) cursor.getFloat(2),
                            new SalaryData(period, (int) cursor.getFloat(3))));
                }
                if (Config.JOB_MANAGER[1].equals(cursor.getString(0))) {//Team lead
                    engineer.add(new Entry(AppUtils.getPeriodIndex(context, period),
                            (int) cursor.getFloat(2),
                            new SalaryData(period, (int) cursor.getFloat(3))));
                }
                if (Config.JOB_MANAGER[2].equals(cursor.getString(0))) {//Project manager
                    juniors.add(new Entry(AppUtils.getPeriodIndex(context, period),
                            (int) cursor.getFloat(2),
                            new SalaryData(period, (int) cursor.getFloat(3))));
                }
            }
        }
        switch (language) {
            case "":
                language = Config.QA;
                lineData = buildLineData(seniors, engineer, juniors, Config.JOB_QA);
                break;
            case Config.MANAGER:
                language = Config.MANAGER;
                lineData = buildLineData(seniors, engineer, juniors, Config.JOB_MANAGER);
                break;
            default:
                lineData = buildLineData(seniors, engineer, juniors, Config.JOB_SOFT);
                break;
        }
        if (lineData.getDataSetCount() > 0) {
            return new LineChartItem(context, lineData, language);
        }
        return null;
    }

    private LineData buildLineData(List<Entry> seniors, List<Entry> engineer,
                                   List<Entry> juniors, String[] jobTitles ){
        List<ILineDataSet> sets = new ArrayList<>();
        if (!seniors.isEmpty()){
            LineDataSet dSenior = buildLineDataSet(seniors, jobTitles[0]);
            sets.add(dSenior);
        }
        if (!engineer.isEmpty()){
            LineDataSet dEngineer = buildLineDataSet(engineer, jobTitles[1]);
            dEngineer.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
            dEngineer.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
            sets.add(dEngineer);
        }
        if (!juniors.isEmpty()) {
            LineDataSet dJunior = buildLineDataSet(juniors, jobTitles[2]);
            dJunior.setColor(ColorTemplate.VORDIPLOM_COLORS[2]);
            dJunior.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[2]);
            sets.add(dJunior);//add data
        }
        return  new LineData(sets);
    }

    private LineDataSet buildLineDataSet(List<Entry> entries, String title){
        LineDataSet result = new LineDataSet(entries, title);
        result.setLineWidth(2.5f);
        result.setCircleRadius(4.5f);
        result.setHighLightColor(Color.rgb(244, 117, 117));
        result.setDrawValues(false);//draw value
        return result;
    }


    /** helper methods */

    private void logTable(String tableName) {
        if (!BuildConfig.DEBUG) return;
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            Timber.d("Table is empty");
        }
        while (!cursor.isAfterLast()) {
            Timber.d(DatabaseUtils.dumpCurrentRowToString(cursor));
            cursor.moveToNext();
        }
    }
}
