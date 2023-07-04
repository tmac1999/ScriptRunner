package com.motorola.scriptrunner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.motorola.scriptrunner.util.CommandResult;
import com.motorola.scriptrunner.util.ShellUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
//https://stackoverflow.com/questions/5383401/android-inject-events-permission

public class MainActivity extends AppCompatActivity {
    public final String TAG = "MainActivity";
    int count = 0;
    private int hourSet;
    private int minSet;
    Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    handler.sendEmptyMessageDelayed(1, delayMillis);
                    long date = System.currentTimeMillis() + 10 * 1000;//（设置的时间是00:00:00,那么现实时间23:59:50+10时，会相等）在设定时间前10秒开始脚本，因为脚本包含了开启录屏的时间
                    String format = ft2.format(new Date(date));
                    String[] splitTime = format.split("-");
                    int sec = Integer.parseInt(splitTime[2]);
                    int min = Integer.parseInt(splitTime[1]);
                    int hour = Integer.parseInt(splitTime[0]);


                    String text = "轮询中：" + count;
                    tv_run_info.setText(text);
                    count++;
                    Log.i(TAG, "start loop:" + format + ",hour:" + hour + ",min:" + min + ",sec:" + sec);
                    Log.i(TAG, "start loop:" + format + ",hourSet:" + hourSet + ",minSet:" + minSet);


                    if (hourSet == hour && minSet == min) {
                        //run script
                        handler.sendEmptyMessage(2);
                    }
                    break;
                case 2:// time hit
                    handler.removeMessages(1);
                    runScript();
                    break;
            }
        }
    };
    int delayMillis = 2 * 1000;

    private void run() {
        String format = ft2.format(new Date(System.currentTimeMillis()));

        String min = format.split("-")[1];
        String hour = format.split("-")[0];
        String hourSet = et_hour.getText().toString();
        String minSet = et_min.getText().toString();
        Log.i(TAG, "start loop:" + format + ",hour:" + hour + ",min:" + min);
        Log.i(TAG, "start loop:" + format + ",hourSet:" + hourSet + ",minSet:" + minSet);


        if (hourSet.equals(hour) && minSet.equals(min)) {
            //run script
            runScript();
        }
        //            Runtime.getRuntime().exec("/data/data/com.motorola.scriptrunner/code_cache/auto_order_court.sh");
//        runScript();
//        CommandResult result = ShellUtils.execCommand("input text \"zhengpeng\"",true,true);
//        final String strResult= String.format(COMMAND_RESULT_FORMAT, result.result, result.successMsg, result.errorMsg);
//        Log.i(TAG, "Runtime.getRuntime:"+strResult);
    }

    String path_8_am = "/system_ext/auto_order_court.sh";
    String path_13_am = "/system_ext/auto_order_court_test.sh";
    String path_exe = path_8_am;

    private void runScript() {
        try {
            Runtime.getRuntime().exec(path_exe);
//            String path = Environment.getExternalStorageDirectory().getPath() + "/auto_order_court.sh";
//            Runtime.getRuntime().exec(path_exe);
            Log.i(TAG, "Runtime.getRuntime:" + path_exe);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String COMMAND_RESULT_FORMAT = "resultCode:%s,successMsg:%s,errorMsg:%s\n";
    SimpleDateFormat ft2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().
                addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
                hourSet = Integer.parseInt(et_hour.getText().toString());

                minSet = Integer.parseInt(et_min.getText().toString());
                Toast.makeText(MainActivity.this, "即将于" + hourSet + ":" + minSet + "开始，请放置于立即预约界面", Toast.LENGTH_LONG).show();
//                handler.sendEmptyMessageDelayed(1, 10 * 1000);//for test
                handler.removeMessages(1);
                handler.sendEmptyMessageDelayed(1, delayMillis);//for test
            }
        });
        findViewById(R.id.tv_network_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread() {
                    int count = 0;

                    @Override
                    public void run() {
                        super.run();
                        while (true) {
                            try {
                                Thread.sleep(3 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            count++;
                            String url = "https://www.baidu.com/";
//                String url = "https://mmibug2go.appspot.com/#/app/report/323003474";
                            request(url);
                            Log.i("RUN", "RUNNING" + count);
                        }
                    }
                }.start();
            }
        });
        TextView tv_run_setting = findViewById(R.id.tv_run_setting);
        tv_run_setting.setOnClickListener(new View.OnClickListener() {
            boolean isTest = false;

            @Override
            public void onClick(View v) {
                if (isTest) {//如果是测试，点击之后变成正式
                    path_exe = path_8_am;
                    isTest = false;
                } else {//如果正式，点击后变测试脚本
                    path_exe = path_13_am;
                    isTest = true;
                }
                tv_run_setting.setText(path_exe);
            }
        });
        et_hour = findViewById(R.id.et_hour);
        et_min = findViewById(R.id.et_min);
        tv_run_info = findViewById(R.id.tv_run_info);
    }

    EditText et_hour, et_min;
    TextView tv_run_info;

    private void request(String url) {
        OkGo.<String>get(url).tag(this).execute(new StringCallback() {

            @Override
            public void onSuccess(Response<String> response) {
                String bodyData = response.body();
//                GmBean data = new Gson().fromJson(bodyData, GmBean.class);
                Log.i("onSuccess", bodyData.toString());
            }


            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Log.i("onError", response.toString());
            }
        });
    }

}