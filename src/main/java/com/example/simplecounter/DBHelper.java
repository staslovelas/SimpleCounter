package com.example.simplecounter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    static String LOG_TAG = "databaseLogs";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "counterDB";
    public static final String TABLE_NAME = "counter";

    public static final String KEY_ID = "id";
    public static final String KEY_VALUE = "value";

    public DBHelper( @Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +
                " ( " + KEY_ID + " integer primary key autoincrement, "
                + KEY_VALUE + " integer " + " ); "
        );
        Log.d(LOG_TAG, "onCreate done");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "onUpgrade done");
    }
}
