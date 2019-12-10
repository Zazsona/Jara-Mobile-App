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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.picasso.Picasso;
import com.zazsona.jaramobileapp.R;
import com.zazsona.jaramobileapp.connectivity.ConnectionManager;
import com.zazsona.jaramobileapp.connectivity.responses.ReportResponse;

import java.io.IOException;
import java.time.Instant;

public class OverviewFragment extends Fragment
{
    private OnOverviewFragmentInteractionListener mListener;

    private ImageView vAvatarImage;
    private TextView vBotName;
    private TextView vCommandCount;
    private TextView vUptime;
    private TextView vOnlineStatus;
    private GraphView vCommandGraph;
    private FrameLayout vLoadingFrame;

    public static final String STATUS_TAG = OverviewFragment.class.getCanonicalName()+".status";
    private ReportResponse status;
    public static final String PAUSE_TIME_TAG = OverviewFragment.class.getCanonicalName()+".pausetime";
    private long pauseSecond;

    public static final String SERIES_TAG = OverviewFragment.class.getCanonicalName()+".series";
    private DataPoint[] graphSeries;

    private BroadcastReceiver minuteBroadcastReceiver;


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
        vAvatarImage = view.findViewById(R.id.imageOnlineStatus);
        vBotName = view.findViewById(R.id.botUserDesc);
        vCommandGraph = view.findViewById(R.id.commandGraph);
        vCommandCount = view.findViewById(R.id.commandsDesc);
        vOnlineStatus = view.findViewById(R.id.statusDesc);
        vUptime = view.findViewById(R.id.uptimeDesc);
        vLoadingFrame = view.findViewById(R.id.loadingFrame);
        Button settingsButton = view.findViewById(R.id.settingsButton);

        drawReportResponse(); //TODO: Remove when design s nulled
        if (savedInstanceState != null)
        {
            pauseSecond = savedInstanceState.getLong(PAUSE_TIME_TAG);
            status = (ReportResponse) savedInstanceState.get(STATUS_TAG);
            if (status == null)
                new ConnectionTask().execute();
            else
                drawReportResponse();
        }
        else
        {
            new ConnectionTask().execute();
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
                new ConnectionTask().execute();
            }
        };
        return view;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        getActivity().unregisterReceiver(minuteBroadcastReceiver);
        pauseSecond = Instant.now().getEpochSecond();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().registerReceiver(minuteBroadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        if (pauseSecond > 0 && (pauseSecond / 60) < (Instant.now().getEpochSecond() / 60))
        {
            status = null;
            new ConnectionTask().execute();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        outState.putSerializable(STATUS_TAG, status);
        outState.putSerializable(PAUSE_TIME_TAG, pauseSecond);
        outState.putSerializable(SERIES_TAG, graphSeries);
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

    private void drawReportResponse()
    {
        if (status != null && status.isOnline())
        {
            Picasso.get().load(status.getProfileImageURL()).into(vAvatarImage);
            vBotName.setText(status.getBotName());
            vOnlineStatus.setText("Online");
            vCommandCount.setText(""+status.getCommandUsageForSession());
            vUptime.setText(formatSecondsToddHHmmss(status.getUptimeSeconds()));
            drawCommandGraph(true);
        }
        else
        {
            vAvatarImage.setImageResource(R.drawable.ic_cloud_off_black_24dp);
            vBotName.setText("Unknown");
            vOnlineStatus.setText("Offline");
            vCommandCount.setText("N/A");
            vUptime.setText("N/A");
            drawCommandGraph(false);
        }

    }

    private void drawCommandGraph(boolean refresh)
    {
        vCommandGraph.removeAllSeries();
        LineGraphSeries<DataPoint> series = null;
        int hoursSinceEpoch = (int) Math.floor(Instant.now().getEpochSecond()/3600.0);
        int hourOfDay = hoursSinceEpoch % 24;
        if (status != null && status.isOnline() && refresh)
        {
            DataPoint[] dataPoints = new DataPoint[24];
            int startingEpochHour = hoursSinceEpoch-23;
            int startingHour = hourOfDay-23;
            for (int i = 0; i<24; i++)
            {
                int xAxisValue = startingHour+i;
                int hourKey = startingEpochHour+i;
                int hourValue = status.getUsageGraph().containsKey(hourKey) ? status.getUsageGraph().get(hourKey) : 0;
                dataPoints[i] = new DataPoint(xAxisValue, hourValue);
            }
            graphSeries = dataPoints;
            series = new LineGraphSeries<>(graphSeries);
            vCommandGraph.addSeries(series);
        }
        else if (graphSeries != null && !refresh)
        {
            series = new LineGraphSeries<>(graphSeries);
            vCommandGraph.addSeries(series);
        }

        vCommandGraph.getViewport().setMaxX(hourOfDay);
        vCommandGraph.getViewport().setMinX(hourOfDay-23);
        vCommandGraph.getViewport().setMinY(0);
        vCommandGraph.getViewport().setXAxisBoundsManual(true);
    }

    private class ConnectionTask extends AsyncTask<String, String, ReportResponse>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            if (status == null)
                vLoadingFrame.setVisibility(View.VISIBLE);
        }

        @Override
        protected ReportResponse doInBackground(String... strings)
        {
            try
            {
                return ConnectionManager.getInstance(getContext()).sendReportRequest();
            }
            catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
                return new ReportResponse();
            }
        }

        @Override
        protected void onPostExecute(ReportResponse reportResponse)
        {
            super.onPostExecute(reportResponse);
            status = reportResponse;
            vLoadingFrame.setVisibility(View.GONE);
            drawReportResponse();
        }
    }

    private String formatSecondsToddHHmmss(Long totalSeconds)
    {
        long remainingTime = totalSeconds;
        long days = remainingTime / (24*60*60);
        remainingTime -= days*(24*60*60);
        long hours = remainingTime / (60*60);
        remainingTime -= hours*(60*60);
        long minutes = remainingTime / (60);
        remainingTime -= minutes*(60);
        long seconds = remainingTime;

        if (days <= 99)
        {
            return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
        }
        else
        {
            return "99:59:59:59+";
        }

    }
}
