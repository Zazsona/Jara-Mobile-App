package com.zazsona.jaramobileapp.connectivity.responses;

import java.util.HashMap;

public class ReportResponse extends Response
{
    private String botName;
    private String profileImageURL;
    private int activeGuilds;
    private int shardCount;
    private boolean online;
    private long uptimeSeconds;
    private double ping;
    private int connectedGuilds;
    private long commandUsageForSession;
    private HashMap<Integer, Integer> usageGraph; //HourSinceEpoch : Commands

    public String getBotName()
    {
        return botName;
    }

    public String getProfileImageURL()
    {
        return profileImageURL;
    }

    public int getActiveGuilds()
    {
        return activeGuilds;
    }

    public int getShardCount()
    {
        return shardCount;
    }

    public boolean isOnline()
    {
        return online;
    }

    public long getUptimeSeconds()
    {
        return uptimeSeconds;
    }

    public double getPing()
    {
        return ping;
    }

    public int getConnectedGuilds()
    {
        return connectedGuilds;
    }

    public long getCommandUsageForSession()
    {
        return commandUsageForSession;
    }

    public HashMap<Integer, Integer> getUsageGraph()
    {
        return usageGraph;
    }
}
