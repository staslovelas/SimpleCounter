package com.example.simplecounter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    counter = intent.getStringExtra("value");
                    TextView txtCnt = (TextView) findViewById(R.id.txtCnt);
                    txtCnt.setText(counter);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(broadcastReceiver, new IntentFilter(MyService.ACTION));
        newService = new MyService();
        newIntent = new Intent(this, newService.getClass());
        startService(newIntent);

        newHelper = new DBHelper(this);

        Log.d(LOG_TAG, "onCreate done");
    }
    public void onClickReset(View v){
        SQLiteDatabase db = newHelper.getWritableDatabase();

        int clearCount = db.delete(DBHelper.TABLE_NAME, null, null);
        Log.d(LOG_TAG, "RESET done: deleted rows count = " + clearCount);
        newHelper.close();

        stopService(newIntent);
        startService(newIntent);

    }

}
