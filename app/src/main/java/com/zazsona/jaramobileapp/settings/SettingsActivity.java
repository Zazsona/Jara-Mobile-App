package com.zazsona.jaramobileapp.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.net.Uri;
import android.os.Bundle;

import com.zazsona.jaramobileapp.R;

public class SettingsActivity extends AppCompatActivity implements SettingsFragment.OnFragmentInteractionListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.settingsLayout, new SettingsFragment()).commit();
    }

    @Override
    public void onNotificationsToggled(boolean value)
    {
        Settings.getInstance(this).setNotifications(value);
        Settings.getInstance(this).save();
    }

    @Override
    public void onIPSet(String ip)
    {
        Settings.getInstance(this).setIp(ip);
        Settings.getInstance(this).save();
    }

    @Override
    public void onPortSet(String port)
    {
        Settings.getInstance(this).setPort(port);
        Settings.getInstance(this).save();
    }
}
