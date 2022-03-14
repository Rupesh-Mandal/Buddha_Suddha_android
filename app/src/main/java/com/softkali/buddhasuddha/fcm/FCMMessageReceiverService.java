package com.softkali.buddhasuddha.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.softkali.buddhasuddha.App;
import com.softkali.buddhasuddha.R;
import com.softkali.buddhasuddha.dashboard.activity.OrderListActivity;



public class FCMMessageReceiverService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("abcd","onMessageReceived");
        Log.e("abcd",remoteMessage.getFrom());

        if (remoteMessage.getData()!=null){
            String title=remoteMessage.getData().get("title");
            String body=remoteMessage.getData().get("body");

            Intent intent=new Intent(this, OrderListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent=PendingIntent.getActivities(this,0, new Intent[]{intent},PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification=new NotificationCompat.Builder(this, App.FCM_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

            int randomNum = 1 + (int)(Math.random() * 1000);

            NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(randomNum,notification);


        }


    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.e("abcd","onDeletedMessages");

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("abcd","onNewToken");

    }
}
