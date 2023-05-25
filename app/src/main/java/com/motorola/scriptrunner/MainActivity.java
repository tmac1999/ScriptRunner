package com.motorola.scriptrunner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.motorola.scriptrunner.util.CommandResult;
import com.motorola.scriptrunner.util.ShellUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
//https://stackoverflow.com/questions/5383401/android-inject-events-permission

public class MainActivity extends AppCompatActivity {
    public final String TAG = "MainActivity";
    Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

//            handler.sendMessageDelayed(handler.obtainMessage(), 3 * 1000);
            run();
        }
    };

    private void run() {
        String format = ft2.format(new Date(System.currentTimeMillis()));

        String min = format.split("-")[1];
        String hour = format.split("-")[0];
        Log.i(TAG, "start loop:" + format + ",min:" + min + ",hour:" + hour);
        if ("07".equals(min)) {
            //run script

        }
        //            Runtime.getRuntime().exec("/data/data/com.motorola.scriptrunner/code_cache/auto_order_court.sh");
        try {
            Runtime.getRuntime().exec("/system_ext/auto_order_court.sh");
            Log.i(TAG, "Runtime.getRuntime:");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        CommandResult result = ShellUtils.execCommand("input text \"zhengpeng\"",true,true);
//        final String strResult= String.format(COMMAND_RESULT_FORMAT, result.result, result.successMsg, result.errorMsg);
//        Log.i(TAG, "Runtime.getRuntime:"+strResult);
    }
    private static final String COMMAND_RESULT_FORMAT="resultCode:%s,successMsg:%s,errorMsg:%s\n";
    SimpleDateFormat ft2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ft2 = new SimpleDateFormat("HH-mm-ss-SSS", Locale.CHINA);
        findViewById(R.id.tv_run).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.i(TAG, "start click");
//                    }
//                }, 3 * 1000);
                handler.sendEmptyMessageDelayed(1,10*1000);
            }
        });
    }

}