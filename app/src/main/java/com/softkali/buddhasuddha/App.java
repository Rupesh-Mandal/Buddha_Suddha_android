package com.softkali.buddhasuddha;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String FCM_CHANNEL_ID="FCM_CHANNEL_ID";
    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel(
                    FCM_CHANNEL_ID,"FCM_Channel", NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
