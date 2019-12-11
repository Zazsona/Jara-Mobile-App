package com.zazsona.jaramobileapp.model;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.zazsona.jaramobileapp.model.connectivity.Settings;

public class SettingsRepository
{
    private static SettingsRepository rInstance;
    private Settings mSettings;

    public static SettingsRepository getInstance(Context context)
    {
        if (rInstance == null)
        {
            rInstance = new SettingsRepository(context);
        }
        return rInstance;
    }

    private SettingsRepository(Context context)
    {
        mSettings = Settings.getInstance(context);
    }

    public Settings getSettings()
    {
        return mSettings;
    }
}
