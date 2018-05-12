package com.example.nhtha.homeworkoutversion2.view;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.nhtha.homeworkoutversion2.view.activity.LoginActivity;
import com.example.nhtha.homeworkoutversion2.view.activity.StartActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by nhtha on 02-Mar-18.
 */

public class NotifiService extends Service {

    public static final String PLAY = "play";
    public static final String CLOSE = "delete";
    private Notification noti;
    private int id;
    private MyReceiver myReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PLAY);
        intentFilter.addAction(CLOSE);
        registerReceiver(myReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        id = intent.getIntExtra("id", 100);
        String date = intent.getStringExtra("date");
        Log.d("DATE", "onStartCommand: " + date);
        if (date != null){
            Calendar calendar = Calendar.getInstance();
            Date date1 = calendar.getTime();
            String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date1.getTime());
            if (date.contains(today)){

                Intent closeIntent = new Intent();
                closeIntent.setType(CLOSE);
                PendingIntent pendingStopIntent = PendingIntent.getBroadcast(this, 1, closeIntent, 0);

                Intent playIntent = new Intent();
                playIntent.setAction(PLAY);
                PendingIntent pendingPlayIntent = PendingIntent.getBroadcast(this, 1, playIntent, 0);

                noti = new Notification.Builder(this)
                        .setOngoing(false)
                        .setAutoCancel(true)
                        .setShowWhen(false)
                        .setSmallIcon(android.R.drawable.ic_media_play)
                        .setContentText("Try Hard ???")
                        .addAction(android.R.drawable.ic_media_play, "Open App", pendingPlayIntent)
                        .addAction(android.R.drawable.ic_menu_delete, "Close", pendingStopIntent)
                        .build();


                startForeground(id, noti);
            }
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case PLAY:
                    Toast.makeText(context, "LET GO", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(NotifiService.this, StartActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                            0,
                            intent1,
                            0);
                    try {
                        pendingIntent.send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                    break;

                case CLOSE:
                    Toast.makeText(context, "CLOSED", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        stopForeground(STOP_FOREGROUND_REMOVE);
                    }
                    break;

                default:
                    break;
            }
        }
    }


}
