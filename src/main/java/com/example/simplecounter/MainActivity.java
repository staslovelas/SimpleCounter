package com.example.simplecounter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    static String LOG_TAG = "activityLogs";
    String counter;
    MyService newService;
    Intent newIntent;
    DBHelper newHelper;
    SQLiteDatabase db;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    db = newHelper.getWritableDatabase();
                    String cnt = intent.getStringExtra("value");
                    Cursor cursor = db.rawQuery("select * from " + DBHelper.TABLE_NAME, null);
                        if(cursor.moveToLast()){
                            counter = cursor.getString(1);
                        }

                        db.close();
                        cursor.close();

                    TextView txtCnt = (TextView) findViewById(R.id.txtCnt);
                    txtCnt.setText(counter);
                }
            });
        }
    };
    //рестарт сервис
   /*private BroadcastReceiver broadcastRestart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            Log.d(LOG_TAG, "Restart done");
            context.startService(new Intent(context, MyService.class));;
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(broadcastReceiver, new IntentFilter(MyService.ACTION_VAL));
        //registerReceiver(broadcastRestart, new IntentFilter(MyService.ACTION_RESTART));//рестарт сервис
        newService = new MyService();
        newIntent = new Intent(this, newService.getClass());
        startService(newIntent);

        newHelper = new DBHelper(this);


        Log.d(LOG_TAG, "onCreate done");
    }
    public void onClickReset(View v){
        db = newHelper.getWritableDatabase();
        int clearCount = db.delete(DBHelper.TABLE_NAME, null, null);
        Log.d(LOG_TAG, "RESET done: deleted rows count = " + clearCount);
        db.close();

        stopService(newIntent);
        startService(newIntent);//закомментить если тестируем рестарт сервиса
    }

}
