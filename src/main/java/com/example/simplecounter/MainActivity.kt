package com.example.simplecounter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    internal var counter: String = ""
    internal var newService: MyService? = null
    internal var newIntent: Intent? = null
    internal var newHelper: DBHelper? = null
    internal var db: SQLiteDatabase? = null

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            runOnUiThread {
                db = newHelper?.writableDatabase
                val cnt = intent.getStringExtra("value")
                val cursor = db?.rawQuery("select * from " + DBHelper.TABLE_NAME, null)
                if (cursor!!.moveToLast()) {
                    counter = cursor.getString(1)
                }

                db?.close()
                cursor.close()

                val txtCnt = findViewById<View>(R.id.txtCnt) as TextView
                txtCnt.text = counter
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerReceiver(broadcastReceiver, IntentFilter(MyService.ACTION_VAL))
        newService = MyService()
        newIntent = Intent(this, newService?.javaClass)
        startService(newIntent)

        newHelper = DBHelper(this)


        Log.d(LOG_TAG, "onCreate done")
    }

    fun onClickReset(v: View) {
        db = newHelper?.writableDatabase
        val clearCount = db?.delete(DBHelper.TABLE_NAME, null, null)
        Log.d(LOG_TAG, "RESET done: deleted rows count = $clearCount")
        db?.close()

        stopService(newIntent)
        startService(newIntent)
    }

    companion object {
        internal var LOG_TAG = "activityLogs"
    }

}
