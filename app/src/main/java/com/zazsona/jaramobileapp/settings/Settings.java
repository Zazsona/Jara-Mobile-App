package com.zazsona.jaramobileapp.settings;

public class Settings
{
    private static String ip = "";
    private static String port = "42995";
    private static boolean notifications = true;

    /**
     * Gets IP
     * @return the ip
     */
    public static String getIp()
    {
        return ip;
    }

    /**
     * Gets port
     *
     * @return port
     */
    public static String getPort()
    {
        return port;
    }

    /**
     * Gets notifications
     *
     * @return notifications
     */
    public static boolean isNotifications()
    {
        return notifications;
    }

    /**
     * Sets the value of ip
     *
     * @param ip the value to set
     */
    protected static void setIp(String ip)
    {
        Settings.ip = ip;
    }

    /**
     * Sets the value of port
     *
     * @param port the value to set
     */
    protected static void setPort(String port)
    {
        Settings.port = port;
    }

    /**
     * Sets the value of notifications
     *
     * @param notifications the value to set
     */
    protected static void setNotifications(boolean notifications)
    {
        Settings.notifications = notifications;
    }
}
