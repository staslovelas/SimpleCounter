package com.example.simplecounter;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    static String LOG_TAG = "serviceLogs";
    static final String ACTION = "com.example.simplecounter.display";
    Intent intent;
    Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate done");
        intent = new Intent(ACTION);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand done");
        timer = new Timer();
        timer.schedule(new CounterTask(), 1000, 1000);
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        Log.d(LOG_TAG, "onDestroy done");
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind done");
        return null;
    }

    class CounterTask extends TimerTask {
        DBHelper helper;
        SQLiteDatabase db;
        int units;

        CounterTask() {
            helper = new DBHelper(getApplicationContext());
        }

        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    db = helper.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put(DBHelper.KEY_VALUE, units);
                    db.insert(DBHelper.TABLE_NAME, null, cv);
                    db.close();

                    Log.d(LOG_TAG, String.valueOf(units));
                    intent.putExtra("value", String.valueOf(units));
                    sendBroadcast(intent);
                }
            }).start();
            units++;
        }
    }
}
