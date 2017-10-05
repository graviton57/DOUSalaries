package com.havrylyuk.dou.data.local.db;

import android.content.Context;
import android.content.Intent;

import com.havrylyuk.dou.data.SyncService;
import com.havrylyuk.dou.data.local.db.DouContract.SalaryEntry;

import org.sqlite.database.sqlite.SQLiteDatabase;
import org.sqlite.database.sqlite.SQLiteOpenHelper;

/**
 * Custom SQLiteOpenHelper with org.sqlite.database.sqlite
 * Created by Igor Havrylyuk on 01.09.2017.
 */

public class DouDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "dou.db";

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;

    private Context context;

    private final String SQL_CREATE_SALARIES_TABLE = "CREATE TABLE " +
            SalaryEntry.TABLE_NAME + " (" +
            SalaryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SalaryEntry.SALARIES_ID + " INTEGER NOT NULL ," +
            SalaryEntry.SALARIES_JOB_TITLE + " TEXT NOT NULL DEFAULT '', " +
            SalaryEntry.SALARIES_PROG_LANG + " TEXT NOT NULL DEFAULT '', " +
            SalaryEntry.SALARIES_SPEC + " TEXT NOT NULL DEFAULT '', " +
            SalaryEntry.SALARIES_EXP + " REAL NOT NULL DEFAULT 0, " +
            SalaryEntry.SALARIES_CUR_JOB_EXP + " REAL NOT NULL DEFAULT 0, " +
            SalaryEntry.SALARIES_PER_MONTH + " REAL NOT NULL DEFAULT 0, " +
            SalaryEntry.SALARIES_CHANGE_12 + " REAL NOT NULL DEFAULT 0, " +
            SalaryEntry.SALARIES_CITY + " TEXT NOT NULL DEFAULT '', " +
            SalaryEntry.SALARIES_SIZE_COMPANY + " TEXT NOT NULL DEFAULT '', " +
            SalaryEntry.SALARIES_TYPE_COMPANY + " TEXT NOT NULL DEFAULT '', " +
            SalaryEntry.SALARIES_GENDER + " TEXT NOT NULL DEFAULT '', " +
            SalaryEntry.SALARIES_AGE + " INTEGER NOT NULL DEFAULT 0, " +
            SalaryEntry.SALARIES_EDUCATION + " TEXT NOT NULL DEFAULT '', " +
            SalaryEntry.SALARIES_UNIVERSITY + " TEXT NOT NULL DEFAULT '', " +
            SalaryEntry.SALARIES_IS_STUDENT + " TEXT NOT NULL DEFAULT 'False', " +
            SalaryEntry.SALARIES_ENG_LEVEL + " TEXT NOT NULL DEFAULT '', " +
            SalaryEntry.SALARIES_SUBJECT_AREA + " TEXT NOT NULL DEFAULT '', " +
            SalaryEntry.SALARIES_PERIOD + " INTEGER NOT NULL ," +
            " UNIQUE (" + SalaryEntry.SALARIES_ID + ", " + SalaryEntry.SALARIES_PERIOD +
            " ) ON CONFLICT REPLACE);";


    public DouDbHelper(Context context, String dbName) {
        super(context, dbName, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SALARIES_TABLE);
        Intent intent = new Intent(context, SyncService.class);
        context.startService(intent);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String deleteTable = "DROP TABLE IF EXISTS ";
        db.execSQL(deleteTable + SalaryEntry.TABLE_NAME);
        onCreate(db);
    }

    public Context getContext() {
        return context;
    }
}
