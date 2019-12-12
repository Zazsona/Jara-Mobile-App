package com.zazsona.jaramobileapp.view.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.zazsona.jaramobileapp.R;
import com.zazsona.jaramobileapp.model.connectivity.Settings;
import com.zazsona.jaramobileapp.viewmodel.SettingsViewModel;

public class SettingsActivity extends AppCompatActivity implements SettingsFragment.OnFragmentInteractionListener
{
    private SettingsViewModel settingsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.settingsLayout, new SettingsFragment()).commit();

        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
    }

    @Override
    public void onNotificationsToggled(boolean value)
    {
        settingsViewModel.getSettings().setNotifications(value);
        settingsViewModel.getSettings().save();
    }

    @Override
    public void onIPSet(String ip)
    {
        settingsViewModel.getSettings().setIp(ip);
        settingsViewModel.getSettings().save();
    }

    @Override
    public void onPortSet(String port)
    {
        settingsViewModel.getSettings().setPort(port);
        settingsViewModel.getSettings().save();
    }
}
