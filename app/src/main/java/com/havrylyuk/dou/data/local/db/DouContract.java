package com.havrylyuk.dou.data.local.db;

import android.provider.BaseColumns;


/**
 * Created by Igor Havrylyuk on 01.09.2017.
 */

public class DouContract {

    public static final class SalaryEntry implements BaseColumns {

        // Table salaries
        public static final String TABLE_NAME              = "salaries";

        public static final String SALARIES_ID             = "salary_id";
        public static final String SALARIES_JOB_TITLE      = "job_title";
        public static final String SALARIES_PROG_LANG      = "prog_lang";
        public static final String SALARIES_SPEC           = "specialization";
        public static final String SALARIES_EXP            = "exp";
        public static final String SALARIES_CUR_JOB_EXP    = "current_job_exp";
        public static final String SALARIES_PER_MONTH      = "per_month";
        public static final String SALARIES_CHANGE_12      = "change_12_month";
        public static final String SALARIES_CITY           = "city";
        public static final String SALARIES_SIZE_COMPANY   = "size_company";
        public static final String SALARIES_TYPE_COMPANY   = "type_company";
        public static final String SALARIES_GENDER         = "gender";
        public static final String SALARIES_AGE            = "age";
        public static final String SALARIES_EDUCATION      = "education";
        public static final String SALARIES_UNIVERSITY     = "university";
        public static final String SALARIES_IS_STUDENT     = "is_student";
        public static final String SALARIES_ENG_LEVEL      = "english_level";
        public static final String SALARIES_SUBJECT_AREA   = "subject_area";
        public static final String SALARIES_PERIOD         = "period";

        public static final String SALARIES_LOWER_QUARTILE = "lower_quartile";
        public static final String SALARIES_MEDIAN         = "median";
        public static final String SALARIES_UPPER_QUARTILE = "upper_quartile";

    }

}
