package com.zazsona.jaramobileapp.overview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.zazsona.jaramobileapp.R;
import com.zazsona.jaramobileapp.ServerPollWorker;
import com.zazsona.jaramobileapp.settings.SettingsActivity;
import java.util.concurrent.TimeUnit;

import static com.zazsona.jaramobileapp.NotificationFactory.ERROR_NOTIF_CHANNEL_ID;

public class MainActivity extends AppCompatActivity implements OverviewFragment.OnOverviewFragmentInteractionListener
{
    private static final String WorkerTag = "com.zazsona.jaramobileapp.workertag";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        createNotificationChannels();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WorkManager workManager = WorkManager.getInstance(this);
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED).build();
        PeriodicWorkRequest pwr = new PeriodicWorkRequest.Builder(ServerPollWorker.class, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS, PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS, TimeUnit.MILLISECONDS)
                .addTag(WorkerTag)
                .setConstraints(constraints)
                .build();
        workManager.enqueueUniquePeriodicWork(WorkerTag, ExistingPeriodicWorkPolicy.REPLACE, pwr);

        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.mainLayout) == null)
        {
            fm.beginTransaction()
                    .add(R.id.mainLayout, OverviewFragment.newInstance())
                    .commit();
        }
    }

    private void createNotificationChannels()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = getString(R.string.errorNotificationChannelName);
            String description = getString(R.string.errorNotificationChannelDesc);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(ERROR_NOTIF_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onSettingsButtonPressed(View view)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
