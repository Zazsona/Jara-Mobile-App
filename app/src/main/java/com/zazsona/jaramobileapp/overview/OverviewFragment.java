package com.zazsona.jaramobileapp.overview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zazsona.jaramobileapp.R;
import com.zazsona.jaramobileapp.connectivity.ConnectionManager;
import com.zazsona.jaramobileapp.connectivity.responses.ReportResponse;

import java.io.IOException;

public class OverviewFragment extends Fragment
{
    public static final String STATUS_TAG = OverviewFragment.class.getCanonicalName()+".status";
    private ReportResponse status;

    private BroadcastReceiver minuteBroadcastReceiver;

    private OnOverviewFragmentInteractionListener mListener;

    public static OverviewFragment newInstance()
    {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        final ImageView onlineStatus = view.findViewById(R.id.imageOnlineStatus);
        Button settingsButton = view.findViewById(R.id.settingsButton);

        if (savedInstanceState != null)
        {
            status = (ReportResponse) savedInstanceState.get(STATUS_TAG);
            if (status == null)
                new ConnectionTask(onlineStatus).execute();
            else
                Picasso.get().load(status.getProfileImageURL()).into(onlineStatus);
        }
        else
        {
            new ConnectionTask(onlineStatus).execute();
        }

        settingsButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                mListener.onSettingsButtonPressed(view);
            }
        });
        minuteBroadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                new ConnectionTask(onlineStatus).execute();
            }
        };
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().registerReceiver(minuteBroadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    public void onPause()
    {
        super.onPause();
        getActivity().unregisterReceiver(minuteBroadcastReceiver);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        outState.putSerializable(STATUS_TAG, status);
        super.onSaveInstanceState(outState);
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

    private class ConnectionTask extends AsyncTask<String, String, ReportResponse>
    {
        private ImageView avatarView;

        public ConnectionTask(ImageView avatarView)
        {
            this.avatarView = avatarView;
        }

        @Override
        protected ReportResponse doInBackground(String... strings)
        {
            try
            {
                status = ConnectionManager.getInstance(getContext()).sendReportRequest();
                return status;
            }
            catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ReportResponse reportResponse)
        {
            super.onPostExecute(reportResponse);
            if (reportResponse != null)
            {
                Picasso.get().load(reportResponse.getProfileImageURL()).into(avatarView);
                Toast.makeText(getContext(), "Commands: "+reportResponse.getCommandUsageForSession(), Toast.LENGTH_LONG).show();
            }
            else
            {
                avatarView.setImageResource(R.drawable.ic_cloud_off_black_24dp);
            }
        }
    }
}
