package com.zazsona.jaramobileapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.zazsona.jaramobileapp.model.ConnectionRepository;
import com.zazsona.jaramobileapp.model.connectivity.responses.ReportResponse;

public class OverviewViewModel extends AndroidViewModel
{
    private ConnectionRepository connectionRepository;
    private LiveData<ReportResponse> status;

    public OverviewViewModel(@NonNull Application application)
    {
        super(application);
        connectionRepository = ConnectionRepository.getInstance(application);
        status = connectionRepository.getJaraStatus();
    }

    public LiveData<ReportResponse> getStatus()
    {
        return status;
    }
}
