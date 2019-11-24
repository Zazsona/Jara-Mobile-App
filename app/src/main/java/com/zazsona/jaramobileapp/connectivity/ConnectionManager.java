package com.zazsona.jaramobileapp.connectivity;
import android.content.Context;

import com.zazsona.jaramobileapp.settings.Settings;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnectionManager implements Serializable
{
    private static ConnectionManager connectionManager;

    private Context context;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private ConnectionManager(Context context)
    {
        this.context = context;
        socket = new Socket();
    }

    public static ConnectionManager getInstance(Context context)
    {
        if (connectionManager == null)
        {
            connectionManager = new ConnectionManager(context);
        }
        return connectionManager;
    }

    public void connect() throws IOException
    {
        socket.connect(new InetSocketAddress(InetAddress.getByName(Settings.getInstance(context).getIp()), Integer.parseInt(Settings.getInstance(context).getPort())), 5000);
    }

    public boolean isConnected()
    {
        return socket.isConnected();
    }

}
