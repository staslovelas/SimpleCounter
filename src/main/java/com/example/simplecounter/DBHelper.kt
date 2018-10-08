package com.example.simplecounter

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table " + TABLE_NAME +
                " ( " + KEY_ID + " integer primary key autoincrement, "
                + KEY_VALUE + " integer " + " ); "
        )
        Log.d(LOG_TAG, "onCreate done")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(LOG_TAG, "onUpgrade done")
    }

    companion object {
        internal var LOG_TAG = "databaseLogs"

        val DATABASE_VERSION = 1
        val DATABASE_NAME = "counterDB"
        val TABLE_NAME = "counter"

        val KEY_ID = "id"
        val KEY_VALUE = "value"
    }
}
