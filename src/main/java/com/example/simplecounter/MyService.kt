package com.example.simplecounter

import android.app.Service
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.IBinder
import android.util.Log

import java.util.ArrayList
import java.util.Timer
import java.util.TimerTask

class MyService : Service() {
    internal var intentValue: Intent? = null
    internal var timer: Timer? = null

    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "onCreate done")
        intentValue = Intent(ACTION_VAL)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(LOG_TAG, "onStartCommand done")
        timer = Timer()
        timer?.schedule(CounterTask(), 1000, 1000)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        Log.d(LOG_TAG, "onDestroy done")
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d(LOG_TAG, "onBind done")
        return null
    }

    internal inner class CounterTask : TimerTask() {
        var helper: DBHelper
        var db: SQLiteDatabase? = null
        var units: Int = 0

        init {
            helper = DBHelper(applicationContext)
        }

        override fun run() {
            Thread(Runnable {
                db = helper.writableDatabase
                val cv = ContentValues()
                cv.put(DBHelper.KEY_VALUE, units)
                db?.insert(DBHelper.TABLE_NAME, null, cv)
                db?.close()

                Log.d(LOG_TAG, units.toString())
                intentValue!!.putExtra("value", units.toString())
                sendBroadcast(intentValue)
            }).start()
            units++
        }
    }

    companion object {
        internal var LOG_TAG = "serviceLogs"
        internal val ACTION_VAL = "com.example.simplecounter.display"
    }
}
