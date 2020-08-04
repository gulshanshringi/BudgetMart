package com.jsrd.budgetmart.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jsrd.budgetmart.R;
import com.jsrd.budgetmart.utils.MyNotificationManager;

public class FirebaseCloudMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseCloudMessagingService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //if the message contains data payload
        //It is a map of custom keyvalues
        //we can read it easily
        if (remoteMessage.getData().size() > 0) {
            //handle the data message here
        }

        //getting the title and the body
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        Log.i("NotificationRec", title);
        createNotification(title, body);
        //then here we can use the title and body to build a notification
    }


    private void createNotification(String title, String body) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(getResources().getString(R.string.CHANNEL_ID), getResources().getString(R.string.CHANNEL_NAME), importance);
            mChannel.setDescription(getResources().getString(R.string.CHANNEL_DESCRIPTION));
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }

        MyNotificationManager.getInstance(this).displayNotification(title, body);
    }
}
