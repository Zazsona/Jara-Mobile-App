package com.zazsona.jaramobileapp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.zazsona.jaramobileapp.connectivity.ConnectionManager;
import com.zazsona.jaramobileapp.overview.MainActivity;

import java.io.IOException;

public class ServerPollWorker extends Worker
{

    public ServerPollWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams)
    {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public Result doWork()
    {
        try
        {
            ConnectionManager connectionManager = ConnectionManager.getInstance(getApplicationContext());
            connectionManager.sendPing();
            //TODO: Do WOrk
        }
        catch (IOException e)
        {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
            notificationManager.notify(1, NotificationFactory.getOfflineNotification(getApplicationContext(), MainActivity.class));
            Log.e(getClass().getName(), e.toString());
        }
        return Result.success();
    }
}
