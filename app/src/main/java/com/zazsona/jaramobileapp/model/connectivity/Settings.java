package com.zazsona.jaramobileapp.model.connectivity;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

public class Settings extends LiveData
{
    private static transient Settings settingsInstance = null;
    private static transient final String fileName = "ConnectionSettings.json";
    private transient Context context;

    @Expose
    private String ip;
    @Expose
    private String port;
    @Expose
    private boolean notifications;

    public static Settings getInstance(Context context)
    {
        if (settingsInstance == null)
        {
            settingsInstance = new Settings(context);
            settingsInstance.restore();
        }
        return settingsInstance;
    }

    private Settings(Context context)
    {
        this.context = context;
    }

    /**
     * Gets IP
     * @return the ip
     */
    public String getIp()
    {
        return ip;
    }

    /**
     * Gets port
     *
     * @return port
     */
    public String getPort()
    {
        return port;
    }

    /**
     * Gets notifications
     *
     * @return notifications
     */
    public boolean isNotifications()
    {
        return notifications;
    }

    /**
     * Sets the value of ip
     *
     * @param ip the value to set
     */
    public void setIp(String ip)
    {
        this.ip = ip;
        postValue(this);
    }

    /**
     * Sets the value of port
     *
     * @param port the value to set
     */
    public void setPort(String port)
    {
        this.port = port.trim();
        postValue(this);
    }

    /**
     * Sets the value of notifications
     *
     * @param notifications the value to set
     */
    public void setNotifications(boolean notifications)
    {
        this.notifications = notifications;
        postValue(this);
    }

    public void save()
    {
        synchronized (settingsInstance)
        {
            try
            {
                File file = new File(context.getFilesDir(), fileName);
                if (!file.exists())
                {
                    file.createNewFile();
                }
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                String json = gson.toJson(this);
                FileOutputStream fos = new FileOutputStream(file);
                PrintWriter pw = new PrintWriter(fos);
                pw.print(json);
                pw.close();
                fos.close();
            }
            catch (IOException e)
            {
                Log.e(this.getClass().getName(), "An error occurred when saving settings.", e);
            }
        }
    }

    private void restore()
    {
        synchronized (settingsInstance)
        {
            try
            {
                File file = new File(context.getFilesDir(), fileName);
                if (file.exists())
                {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = new String(Files.readAllBytes(file.toPath()));
                    Settings settingsFromFile = gson.fromJson(json, Settings.class);

                    setIp(settingsFromFile.getIp());
                    setPort(settingsFromFile.getPort());
                    setNotifications(settingsFromFile.isNotifications());
                }
                else
                {
                    setIp("");
                    setPort("42995");
                    setNotifications(true);
                }

            }
            catch (IOException e)
            {
                Log.e(this.getClass().getName(), "An error occurred when restoring settings.", e);
                return;
            }
            postValue(this);
        }
    }
}
