package com.havrylyuk.dou;



import com.havrylyuk.dou.data.local.db.DouContract.SalaryEntry;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by Igor Havrylyuk on 06.09.2017.
 */

public class Config  {

    public static final String CSV_FILE_PREFIX = "_mini.csv";
    public static final String MIN_RESULT_COUNT = "1";
    public static final String CHART_DATE_FORMAT = "MMM yyyy";

    public static final String EXTENSIONS_LIB   = "libsqlitefunctions";

    //Csv file headers for converter
    public static final Map<String,String> HEADER_SET = new HashMap<>();

    static {
        HEADER_SET.put("N"                    , SalaryEntry.SALARIES_ID);
        HEADER_SET.put("Должность"            , SalaryEntry.SALARIES_JOB_TITLE);
        HEADER_SET.put("Язык.программирования", SalaryEntry.SALARIES_PROG_LANG);
        HEADER_SET.put("Специализация"        , SalaryEntry.SALARIES_SPEC);
        HEADER_SET.put("exp"                  , SalaryEntry.SALARIES_EXP);
        HEADER_SET.put("Общий.опыт.работы"    , SalaryEntry.SALARIES_EXP);// for May 2012, Dec 2011
        HEADER_SET.put("current_job_exp"      , SalaryEntry.SALARIES_CUR_JOB_EXP);
        HEADER_SET.put("Зарплата.в.месяц"     , SalaryEntry.SALARIES_PER_MONTH);
        HEADER_SET.put("salary"               , SalaryEntry.SALARIES_PER_MONTH);//for May 2012, Dec 2011
        HEADER_SET.put("Изменение.зарплаты"   , SalaryEntry.SALARIES_CHANGE_12);//before Dec 2015
        HEADER_SET.put("Изменение.зарплаты.за.12.месяцев", SalaryEntry.SALARIES_CHANGE_12);//after Dec 2015
        HEADER_SET.put("Город"                , SalaryEntry.SALARIES_CITY);
        HEADER_SET.put("Размер.компании"      , SalaryEntry.SALARIES_SIZE_COMPANY);
        HEADER_SET.put("Тип.компании"         , SalaryEntry.SALARIES_TYPE_COMPANY);
        HEADER_SET.put("Пол"                  , SalaryEntry.SALARIES_GENDER);
        HEADER_SET.put("Возраст"              , SalaryEntry.SALARIES_AGE);
        HEADER_SET.put("Образование"          , SalaryEntry.SALARIES_EDUCATION);
        HEADER_SET.put("Университет"          , SalaryEntry.SALARIES_UNIVERSITY);// Added after Dec 2015
        HEADER_SET.put("Еще.студент"          , SalaryEntry.SALARIES_IS_STUDENT);
        HEADER_SET.put("Уровень.английского"  , SalaryEntry.SALARIES_ENG_LEVEL);
        HEADER_SET.put("Предметная.область"   , SalaryEntry.SALARIES_SUBJECT_AREA);//+
    }

    public static final String[] LANGUAGES = new String[]{"Java", "JavaScript", "C#/.NET", "C++",
            "Objective-C", "PHP", "Python", "Ruby/Rails", "Other"};

    public static final String[] JOB_SOFT = new String[]{
            "Senior Software Engineer", "Software Engineer", "Junior Software Engineer"};

    public static final String[] JOB_QA = new String[]{
            "Senior QA engineer", "QA engineer", "Junior QA engineer"};

    public static final String[] JOB_MANAGER = new String[]{
            "Senior Project Manager / Program Manager", "Team lead", "Project manager"};

    public static final String MANAGER = "Management";
    public static final String QA = "QA";

    public static final int JOBS_LENGTH = 16;//max length job title in charts items
    public static final int SALARIES_IN_CITY = 100;

}
