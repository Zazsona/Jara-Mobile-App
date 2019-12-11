package com.zazsona.jaramobileapp.model.connectivity.responses;

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

    public static ReportResponse getOfflineResponse()
    {
        ReportResponse offlineResponse = new ReportResponse();
        offlineResponse.botName = "N/A";
        offlineResponse.profileImageURL = null;
        offlineResponse.activeGuilds = 0;
        offlineResponse.shardCount = 0;
        offlineResponse.online = false;
        offlineResponse.uptimeSeconds = 0;
        offlineResponse.ping = 0;
        offlineResponse.connectedGuilds = 0;
        offlineResponse.commandUsageForSession = 0;
        offlineResponse.usageGraph = new HashMap<>();
        return offlineResponse;
    }

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
