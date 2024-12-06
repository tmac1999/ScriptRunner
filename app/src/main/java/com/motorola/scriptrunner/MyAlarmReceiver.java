package com.motorola.scriptrunner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyAlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm went off", Toast.LENGTH_SHORT).show();
        Log.i("MyAlarmReceiver","onReceive:intent"+intent.toString());
        MainActivity.runScript();
    }
}
