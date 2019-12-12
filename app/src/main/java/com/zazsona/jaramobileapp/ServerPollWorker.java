package com.zazsona.jaramobileapp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.zazsona.jaramobileapp.model.ConnectionRepository;
import com.zazsona.jaramobileapp.model.connectivity.responses.ReportResponse;
import com.zazsona.jaramobileapp.view.overview.MainActivity;

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
        ReportResponse reportResponse = ConnectionRepository.getInstance(getApplicationContext()).getJaraStatus().getValue();
        if (!reportResponse.isOnline())
        {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
            notificationManager.notify(1, NotificationFactory.getOfflineNotification(getApplicationContext(), MainActivity.class));
            Log.e(getClass().getName(), "Jara is offline.");
        }
        return Result.success();
    }
}
