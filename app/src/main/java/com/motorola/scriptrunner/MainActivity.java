package com.motorola.scriptrunner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Timer;
import java.util.TimerTask;
//https://stackoverflow.com/questions/5383401/android-inject-events-permission

public class MainActivity extends AppCompatActivity { //todo  预定国网
    public static final String TAG = "MainActivity";
    int count = 0;
    private int hourSet;
    private int minSet;
    Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
//                    handler.sendEmptyMessageDelayed(1, delayMillis);
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
                    Log.i(TAG, "start loop:" + format + ",hourSet:" + hourSet + ",minSet:" + minSet + "count:" + count);


                    if (hourSet == hour && minSet == min) {
                        //run script
                        timer.cancel();
                        handler.sendEmptyMessage(2);
                    }
                    break;
                case 2:// time hit
                    handler.removeMessages(1);
                    timer.cancel();
                    runScript();
                    break;
            }
        }
    };
    int delayMillis = 2 * 1000;


    static String path_8_am = "/system_ext/auto_order_court.sh";
    static String path_13_am = "/system_ext/auto_order_court_test.sh";
    static String path_exe = path_8_am;

    public static void runScript() {
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
                String hour = et_hour.getText().toString();
                hourSet = Integer.parseInt(hour.equals("") ? "0" : hour);
//
                String min = et_min.getText().toString();
                minSet = Integer.parseInt(min.equals("") ? "0" : min);


                Log.i(TAG, "Runtime.getRuntime:onClick");

//                AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//                Intent intent = new Intent(MainActivity.this, MyAlarmReceiver.class);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
//                Calendar time = Calendar.getInstance();
//                time.setTimeInMillis(System.currentTimeMillis());
//                time.add(Calendar.SECOND, 30);
//                alarmMgr.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);

                setAlarm();

            }
        });

        findViewById(R.id.tv_network_request).

                setOnClickListener(new View.OnClickListener() {
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

    TimerTask task = new TimerTask() {

        @Override

        public void run() {


        }

    };

    Timer timer = new Timer();
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

    public void setAlarm() {
        final Calendar date;
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        date.set(Calendar.SECOND, 0);

                        Log.i(TAG, "The choosen one " + date.getTime() + ",hourOfDay:" + hourOfDay + ",minute:" + minute);
                        hourSet = hourOfDay;
                        minSet = minute;
                        TextView tv_run_info_time = findViewById(R.id.tv_run_info_time);
                        tv_run_info_time.setText(ft2.format(date.getTime()));
                        long timeInMillis = date.getTimeInMillis()-10*1000;//提前10秒执行闹钟 打开录屏
                        startAlarm(timeInMillis);
                        Toast.makeText(MainActivity.this, "即将于" + hourSet + ":" + minSet + "开始，请放置于立即预约界面", Toast.LENGTH_LONG).show();

                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
//        DateFormat dateFormat = 安卓.text.format.DateFormat.getDateFormat(getApplicationContext());

    }

    private void startAlarm(long timeInMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //Passing data
        String title = "mTitle.getText().toString()";
        String message = "mMessage.getText().toString()";

        int id = (int) System.currentTimeMillis();
        Intent intent = new Intent(this, MyAlarmReceiver.class);
        intent.putExtra("Title", title);
        intent.putExtra("Message", message);
        intent.putExtra("ID", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_IMMUTABLE);

        String format = ft2.format(new Date(timeInMillis));
        Log.i(TAG, "startAlarm setExact: " + format);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);

    }
}