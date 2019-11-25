package com.zazsona.jaramobileapp;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

public class NotificationFactory
{
    public static final String ERROR_NOTIF_CHANNEL_ID = "com.zazsona.jaramobileapp.errorNotif";

    public static Notification getOfflineNotification(Context context, Class targetClass)
    {
        Intent intent = new Intent(context, targetClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        int argb = Color.argb(0, 0, 145, 212);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ERROR_NOTIF_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_cloud_off_black_24dp)
                .setContentTitle("Jara")
                .setContentText("The bot has gone offline.")
                .setLights(argb, 1000, 500)
                .setContentIntent(pi)
                .setAutoCancel(true);

        return builder.build();
    }
}
