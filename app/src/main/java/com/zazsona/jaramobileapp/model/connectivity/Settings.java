package com.zazsona.jaramobileapp.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

public class Settings
{
    private static transient Settings settingsInstance = null;
    private static transient final String fileName = "ConnectionSettings.json";
    private transient Context context;

    private String ip;
    private String port;
    private boolean notifications;

    public static Settings getInstance(Context context)
    {
        if (settingsInstance == null)
            settingsInstance = new Settings(context);

        return settingsInstance;
    }

    private Settings(Context context)
    {
        this.context = context;
        restore();
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
    }

    /**
     * Sets the value of port
     *
     * @param port the value to set
     */
    public void setPort(String port)
    {
        this.port = port.trim();
    }

    /**
     * Sets the value of notifications
     *
     * @param notifications the value to set
     */
    public void setNotifications(boolean notifications)
    {
        this.notifications = notifications;
    }

    public synchronized void save()
    {
        try
        {
            File file = new File(context.getFilesDir(), fileName);
            if (!file.exists())
            {
                file.createNewFile();
            }
            Gson gson = new Gson();
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

    private synchronized void restore()
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
    }
}
