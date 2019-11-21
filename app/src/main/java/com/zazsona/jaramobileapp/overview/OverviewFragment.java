package com.zazsona.jaramobileapp.overview;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.zazsona.jaramobileapp.R;
import com.zazsona.jaramobileapp.connectivity.ConnectionManager;
import com.zazsona.jaramobileapp.settings.Settings;
import com.zazsona.jaramobileapp.settings.SettingsActivity;

import java.io.IOException;

public class OverviewFragment extends Fragment
{
    private OnOverviewFragmentInteractionListener mListener;

    public static OverviewFragment newInstance()
    {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        final ImageView onlineStatus = view.findViewById(R.id.imageOnlineStatus);
        new AsyncTask<String, String, String>()
        {

            @Override
            protected String doInBackground(String... strings)
            {
                try{Thread.sleep(1000);}catch(InterruptedException e){e.printStackTrace();};
                try{ConnectionManager.getInstance().connect();}catch(IOException e){e.printStackTrace();};
                return null;
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                if (ConnectionManager.getInstance().isConnected())
                {
                    onlineStatus.setImageResource(R.drawable.ic_cloud_black_24dp);
                }
                else
                {
                    onlineStatus.setImageResource(R.drawable.ic_cloud_off_black_24dp);
                }
            }
        }.execute();
        Button settingsButton = view.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                mListener.onSettingsButtonPressed(view);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnOverviewFragmentInteractionListener)
        {
            mListener = (OnOverviewFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                                               + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnOverviewFragmentInteractionListener
    {
        public void onSettingsButtonPressed(View view);
    }
}
