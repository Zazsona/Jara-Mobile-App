package com.zazsona.jaramobileapp.connectivity;
import android.content.Context;

import com.google.gson.Gson;
import com.zazsona.jaramobileapp.connectivity.requests.ReportRequest;
import com.zazsona.jaramobileapp.connectivity.responses.ReportResponse;
import com.zazsona.jaramobileapp.settings.Settings;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnectionManager
{
    private static ConnectionManager connectionManager;

    private Gson gson;
    private Context context;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private ConnectionManager(Context context)
    {
        this.context = context;
        this.gson = new Gson();
    }

    public static ConnectionManager getInstance(Context context)
    {
        if (connectionManager == null)
        {
            connectionManager = new ConnectionManager(context);
        }
        return connectionManager;
    }

    private void connect() throws IOException
    {
        socket = new Socket();
        socket.connect(new InetSocketAddress(
                InetAddress.getByName(Settings.getInstance(context).getIp()),
                Integer.parseInt(Settings.getInstance(context).getPort())),
                       5000);
        ois = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());
    }

    private void disconnect() throws IOException
    {
        socket.close();
        ois.close();
        oos.close();
        socket = null;
        ois = null;
        oos = null;
    }

    public boolean sendPing() throws IOException
    {
        connect();
        boolean success = isConnected();
        disconnect();
        return success;
    }

    public ReportResponse sendReportRequest() throws IOException, ClassNotFoundException
    {
        connect();
        ReportRequest reportRequest = new ReportRequest();
        String requestJson = gson.toJson(reportRequest);
        oos.writeObject(requestJson);
        oos.flush();
        String responseJson = (String) ois.readObject();
        disconnect();
        ReportResponse reportResponse = gson.fromJson(responseJson, ReportResponse.class);
        return reportResponse;
    }

    public boolean isConnected()
    {
        return socket.isConnected();
    }

}
