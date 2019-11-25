package com.zazsona.jaramobileapp.connectivity.responses;

import java.io.Serializable;

public class Response implements Serializable
{
    private String jaraVersion;

    public Response()
    {
        this.jaraVersion = "0.1";
    }

    public String getJaraVersion()
    {
        return jaraVersion;
    }
}
