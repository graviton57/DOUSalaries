package com.havrylyuk.dou.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.inputmethod.InputMethodManager;

import com.havrylyuk.dou.Config;
import com.havrylyuk.dou.R;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

/**
 * Created by Igor Havrylyuk on 02.09.2017.
 */

public class AppUtils {



    public static final DateTimeFormatter FILES_FORMATTER =
            DateTimeFormat.forPattern(Config.CHART_DATE_FORMAT).withLocale(Locale.US);

    private static final String EXP_OVER_10_YEARS = "10"; //over 11 years
    private static final String EXP_LESS_3_MONTH = "0"; //less than 3 month's

    /**
     * Check for internet connection
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Hide keyboard
     */
    public static void hideKeyboard(Context context) {
        InputMethodManager inputManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (((Activity) context).getCurrentFocus() != null) {
            try {
                inputManager.hideSoftInputFromWindow(
                        ((AppCompatActivity) context).getCurrentFocus().getWindowToken(), 0);
            } catch (NullPointerException ex) {
                //
            }
        }
    }

    /**
     * Retrieve the quartile value from an array
     * .
     * @param values The array of data
     * @param lowerPercent The percent cut off. For the lower quartile use 25,
     *      50 for the median, for the upper-quartile use 75
     * @return The quartile value
     */
    public static double quartile(double[] values, double lowerPercent) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("The data array either is null" +
                    " or does not contain any data.");
        }
        double[] v = new double[values.length];
        System.arraycopy(values, 0, v, 0, values.length);
        Arrays.sort(v);
        int n = (int) Math.round(v.length * lowerPercent / 100);
        return v[n];
    }

    /*
    * Convert string date 'Dec 2016' to string '2016_dec_mini.csv'
    */
    public static String getFileNameFromDate(String date)
            throws IllegalArgumentException, UnsupportedOperationException{
        return FILES_FORMATTER.parseDateTime(date).toString("yyyy_MMM", Locale.US)
                .toLowerCase() + Config.CSV_FILE_PREFIX;
    }

    /*
    * Convert string date 'Dec 2016' to long value
    */
    public static long convertDate(String date)
            throws IllegalArgumentException, UnsupportedOperationException {
        return  FILES_FORMATTER.parseDateTime(date).getMillis();
    }

    /*
     * Get experience of string
     */
    public static String calcExperienceOfString(Context context, String experience){
        if (context.getString(R.string.experience_over_doxuya).equals(experience)){
            Timber.d("exp=%s", experience);
            return EXP_OVER_10_YEARS;
        } else if (context.getString(R.string.experience_april).equals(experience)){
            Timber.d("exp=%s", experience);
            return EXP_LESS_3_MONTH;
        } else return experience;
    }

   /*
    * Return string index for selected period
    */
    public static int getPeriodIndex(Context context, String period){
        final String[] dates = reverseArray(context.getResources().getStringArray(R.array.array_period_date));
        for (int i = 0; i < dates.length; i++) {
            if (dates[i].equals(period)){
                return i;
            }
        }
        return RecyclerView.NO_POSITION;
    }

    /*
    * Return string index for selected job title
    */
    public static int getJobTitleIndex(Context context, String jobTitle){
        final String[] dates = reverseArray(context.getResources().getStringArray(R.array.array_job_title));
        for (int i = 0; i < dates.length; i++) {
            if (dates[i].equals(jobTitle)){
                return i;
            }
        }
        return RecyclerView.NO_POSITION;
    }

    /*
    * Reverse array of string
    */
    public static String[] reverseArray(String[] data){
        if (data == null) {
            data = new String[0];
        }
        List<String> result = Arrays.asList(data);
        Collections.reverse(result);
        return result.toArray(new String[result.size()]);
    }

    /*
    * Format html
    */
    @SuppressWarnings({"deprecation"})
    public static CharSequence fromHtml(@NonNull String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(text);
        }
    }

    /**
     * Rename old city name
     * @param context Context
     * @param city old city name
     * @return String - new city name
     */
    public static String renameCity(@NonNull Context context, @NonNull String city){
        if (context.getString(R.string.city_kirovograd).equals(city)){
            return context.getString(R.string.city_kropiv);
        }
        if (context.getString(R.string.city_dnipropetrovsk).equals(city)){
            return context.getString(R.string.city_dnipro);
        }
        return city;
    }

    /**
     * Check if job title is QA or Management
     * @param jobTitle checked lob title
     */
    public static boolean checkProgramming(String jobTitle) {
        for (String s : Config.JOB_QA) {
            if (s.equals(jobTitle)) {
                return true;
            }
        }
        for (String s : Config.JOB_MANAGER) {
            if (s.equals(jobTitle)) {
                return true;
            }
        }
        return false;
    }


}