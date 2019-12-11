package com.zazsona.jaramobileapp.model;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.zazsona.jaramobileapp.model.connectivity.ConnectionManager;
import com.zazsona.jaramobileapp.model.connectivity.responses.ReportResponse;

public class ConnectionRepository
{
    private static ConnectionRepository rInstance;
    private ConnectionManager rConnectionManager;
    private MutableLiveData<ReportResponse> rReportResponse;

    public static ConnectionRepository getInstance(Context context)
    {
        if (rInstance == null)
        {
            rInstance = new ConnectionRepository(context);
        }
        return rInstance;
    }

    private ConnectionRepository(final Context context)
    {
        rConnectionManager = ConnectionManager.getInstance(context);
        rReportResponse = new MutableLiveData<>();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    while (true)
                    {
                        ReportResponse reportResponse = rConnectionManager.sendReportRequest();
                        rReportResponse.postValue(reportResponse);
                        Thread.sleep(10*1000);
                    }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                    Toast errorToast = Toast.makeText(context, "Error updating stats.", Toast.LENGTH_LONG);
                    errorToast.show();
                }
            }
        }).start();
    }

    public MutableLiveData<ReportResponse> getJaraStatus()
    {
        return rReportResponse;
    }
}
