package com.android.readsms;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        } else {
            IntentFilter filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
            registerReceiver(mMessageReceiver, filter);
        }

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msgData = "";
            Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"),
                    null, null, null, null);
            if (cursor.moveToFirst()) { // must check the result to prevent exception
                do {
                    for(int idx=0;idx<cursor.getColumnCount();idx++){
                        msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                    }
            } while (cursor.moveToNext());
            }
            Toast.makeText(context, msgData.substring(0, 25), Toast.LENGTH_LONG).show();
            Log.d("receiver", "Got message: " + msgData);
        }
    };
}
