package com.zazsona.jaramobileapp.connectivity.requests;

import java.io.Serializable;

public class Request implements Serializable
{
    private String jaraVersion;
    private RequestType requestType;

    public Request(RequestType requestType)
    {
        this.jaraVersion = "0.1";
        this.requestType = requestType;
    }

    public String getJaraVersion()
    {
        return jaraVersion;
    }

    public RequestType getRequestType()
    {
        return requestType;
    }
}
