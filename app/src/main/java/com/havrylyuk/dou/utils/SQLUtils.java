package com.havrylyuk.dou.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.havrylyuk.dou.R;
import com.havrylyuk.dou.data.local.db.DouContract.SalaryEntry;
import com.havrylyuk.dou.ui.main.demographics.DemographicsType;

/**
 * SQL Utils Class
 * Created by Igor Havrylyuk on 06.09.2017.
 */

public class SQLUtils {


    //Cities that participate in the sample
    public static final String[] CITIES = {"Киев", "Львов", "Харьков", "Днепр", "Одесса"};
    //Job titles that participate in the sample
    public static final String[] JOB_TITLES = {"Senior Software Engineer","Software Engineer",
            "Junior Software Engineer"};


    /*
    * Build selection string for salaries with period cursor loader
    * Return's  selection string
    */
    /*WHERE  job_title IN ('Senior Software Engineer','Software Engineer','Junior Software Engineer',
                                 'Senior Project Manager / Program Manager','Team lead','Project manager',
                                 'Senior QA engineer','QA engineer','Junior QA engineer' ) AND
    city IN('Киев','Львов','Харьков','Днепр','Одесса')  AND period = '2016_dec_raw.csv'*/
    public static String buildSelectionsWithYear(@NonNull Context context) {
        return   SalaryEntry.SALARIES_JOB_TITLE +
                " IN ('Senior Software Engineer','Software Engineer','Junior Software Engineer'," +
                " 'Senior QA engineer','QA engineer','Junior QA engineer'," +
                " 'Senior Project Manager / Program Manager','Team lead','Project manager') " +
                " AND " +
                SalaryEntry.SALARIES_CITY + " IN ('" +
                context.getString(R.string.city_kiev)    + "','" +
                context.getString(R.string.city_lviv)    + "','" +
                context.getString(R.string.city_kharkiv) + "','" +
                context.getString(R.string.city_dnipro)  + "','" +
                context.getString(R.string.city_odessa)  + "') " +
                " AND " + SalaryEntry.SALARIES_PERIOD + " = ? ";

    }
    /*
     * Build selection arguments array for salaries with year cursor loader
     * Return's  selection args array
    */
    public static String[] buildSelectionsArgsWithYar(String period) {
        return period == null ? null : new String[]{period};
    }

    /*
    * Build selection string for salaries with city cursor loader
    * Return's  selection string
    */
    public static String buildSelectionsWithCity(@NonNull Context context, String selectedCity) {
        StringBuilder result = new StringBuilder();
        if (!selectedCity.equals(context.getString(R.string.all_ukraine))){
            if (selectedCity.equals(context.getString(R.string.city_not_kiev))){
                result.append(SalaryEntry.SALARIES_CITY).append(" <> ?  AND ");
            } else {
                result.append(SalaryEntry.SALARIES_CITY).append(" = ?  AND ");
            }
        }
        result.append(SalaryEntry.SALARIES_JOB_TITLE)// TODO: 09.09.2017 remove hardcode job titles strings
                .append(" IN ('Senior Software Engineer', 'Software Engineer', 'Junior Software Engineer'," +
                        " 'Senior QA engineer', 'QA engineer', 'Junior QA engineer'," +
                        " 'Senior Project Manager / Program Manager', 'Team lead', 'Project manager')");
        return result.toString();
    }

    /*
    * Build selection arguments array for salaries with city cursor loader
    * Return's  selection args array
    */
    public static String[] buildSelectionsArgsWithCity(@NonNull Context context, String selectedCity) {
        if (selectedCity.equals(context.getString(R.string.all_ukraine))) {
            return null;
        } else {
            if (selectedCity.equals(context.getString(R.string.city_not_kiev))) {//"не Киев"
                return new String[]{context.getString(R.string.city_kiev)};// city <> 'Киев'
            } else {
                return new String[]{selectedCity};
            }
        }
    }

    public static final String[] columnsForWidget = new String[]{
            " lower_quartile(" + SalaryEntry.SALARIES_PER_MONTH + ") AS " +
                    SalaryEntry.SALARIES_LOWER_QUARTILE,
            " median(" + SalaryEntry.SALARIES_PER_MONTH + ") AS " +
                    SalaryEntry.SALARIES_MEDIAN,
            " upper_quartile(" + SalaryEntry.SALARIES_PER_MONTH + ") AS " +
                    SalaryEntry.SALARIES_UPPER_QUARTILE,
            " count(*) AS " + SalaryEntry._COUNT};

    public static final String[] columnsForCitiesAndYears = new String[]{
            SalaryEntry.SALARIES_JOB_TITLE,
            SalaryEntry.SALARIES_PROG_LANG,
            " median(" + SalaryEntry.SALARIES_PER_MONTH + ") AS " + SalaryEntry.SALARIES_MEDIAN,
            " count(*) AS " + SalaryEntry._COUNT,
            SalaryEntry.SALARIES_PERIOD,
            SalaryEntry.SALARIES_CITY};

    //build selections string for query salary widget fragment loader
    public static String buildSelectionsForWidget(@NonNull Context context, String city, String language){
        StringBuilder result = new StringBuilder();
        result.append(SalaryEntry.SALARIES_PERIOD).append(" = ? AND ");
        if (!context.getString(R.string.language_none).equals(language)){
            result.append(SalaryEntry.SALARIES_PROG_LANG).append(" = ? AND ");
        }
        if (!context.getString(R.string.all_ukraine).equals(city)){
            if (context.getString(R.string.city_not_kiev).equals(city)){
                result.append(SalaryEntry.SALARIES_CITY).append(" <> ?  AND ");
            } else {
                result.append(SalaryEntry.SALARIES_CITY).append(" = ?  AND ");
            }
        }
        result.append(SalaryEntry.SALARIES_JOB_TITLE).append(" = ? AND ")
                .append(SalaryEntry.SALARIES_EXP).append(" >= ? ");
        return result.toString();
    }

    //build selections arguments for query salary loader widget fragment
    public static String[] buildSelectionsForWidgetArgs(@NonNull Context context, String period, String language,
                                                        String city, String jobTitle, int experience){
        StringBuilder result = new StringBuilder();
        result.append(period).append(",");
        if (!context.getString(R.string.language_none).equals(language)){
            result.append(language).append(",");
        }
        if (!context.getString(R.string.all_ukraine).equals(city)){
            if (context.getString(R.string.city_not_kiev).equals(city)) {
                result.append(context.getString(R.string.city_kiev)).append(",");// city <> 'Киев'
            } else {
                result.append(city).append(",");
            }
        }
        result.append(jobTitle).append(",").append(String.valueOf(experience));
        return result.toString().split(",");
    }


    /*
     * Return's selections array in depending on the demographic chart type
    */
    public static String[] buildProjectionForDemographics(DemographicsType demographic) {
        String s;
        switch (demographic){
            case GENDER:
                s = SalaryEntry.SALARIES_GENDER;
                break;
            case AGE:
                s = SalaryEntry.SALARIES_AGE;
                break;
            case EXPERIENCE:
                s = SalaryEntry.SALARIES_EXP;
                break;
            case ENG_LEVEL:
                s = SalaryEntry.SALARIES_ENG_LEVEL;
                break;
            case CITY:
                s = SalaryEntry.SALARIES_CITY;
                break;
            case EDUCATION:
                s = SalaryEntry.SALARIES_EDUCATION;
                break;
            case COMPANY_SIZE:
                s = SalaryEntry.SALARIES_SIZE_COMPANY;
                break;
            case COMPANY_TYPE:
                s = SalaryEntry.SALARIES_TYPE_COMPANY;
                break;
            case JOB_TITLE:
                s = SalaryEntry.SALARIES_JOB_TITLE;
                break;
            default:return null;
        }
        return new String[]{s, " COUNT(" + s + ") AS " + SalaryEntry._COUNT};
    }

    public static String buildGroupByForDemographics(DemographicsType demographic) {
        String s;
        switch (demographic){
            case GENDER:
                s = SalaryEntry.SALARIES_GENDER;
                break;
            case AGE:
                s = SalaryEntry.SALARIES_AGE;
                break;
            case EXPERIENCE:
                s = SalaryEntry.SALARIES_EXP;
                break;
            case ENG_LEVEL:
                s = SalaryEntry.SALARIES_ENG_LEVEL;
                break;
            case CITY:
                s = SalaryEntry.SALARIES_CITY;
                break;
            case EDUCATION:
                s = SalaryEntry.SALARIES_EDUCATION;
                break;
            case COMPANY_SIZE:
                s = SalaryEntry.SALARIES_SIZE_COMPANY;
                break;
            case COMPANY_TYPE:
                s = SalaryEntry.SALARIES_TYPE_COMPANY;
                break;
            case JOB_TITLE:
                s = SalaryEntry.SALARIES_JOB_TITLE;
                break;
            default:return null;
        }
        return s;
    }

    /*
     * Return's order by depending on the DemographicsType
     */
    public static String buildSortOrderForDemographics(DemographicsType type) {
        switch (type){
            case JOB_TITLE:
                return SalaryEntry._COUNT + " ASC ";
        }
        return null;
    }


}
