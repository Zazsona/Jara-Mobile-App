package com.zazsona.jaramobileapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.zazsona.jaramobileapp.model.SettingsRepository;
import com.zazsona.jaramobileapp.model.connectivity.Settings;

public class SettingsViewModel extends AndroidViewModel
{
    private SettingsRepository settingsRepository;
    private Settings settings;

    public SettingsViewModel(@NonNull Application application)
    {
        super(application);
        settingsRepository = SettingsRepository.getInstance(application);
        settings = settingsRepository.getSettings();
    }

    public Settings getSettings()
    {
        return settings;
    }
}
