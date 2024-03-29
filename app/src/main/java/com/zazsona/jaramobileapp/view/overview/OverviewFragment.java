package com.zazsona.jaramobileapp.view.overview;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
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
import com.zazsona.jaramobileapp.model.connectivity.responses.ReportResponse;
import com.zazsona.jaramobileapp.viewmodel.OverviewViewModel;

import java.time.Instant;

public class OverviewFragment extends Fragment
{
    private OverviewViewModel overviewViewModel;

    private OnOverviewFragmentInteractionListener mListener;

    private ImageView vAvatarImage;
    private TextView vBotName;
    private TextView vCommandCount;
    private TextView vUptime;
    private TextView vOnlineStatus;
    private GraphView vCommandGraph;
    private FrameLayout vLoadingFrame;
    private Button vSettingsButton;

    public static final String PAUSE_TIME_TAG = OverviewFragment.class.getCanonicalName()+".pausetime";
    private long pauseSecond;

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
        overviewViewModel = new ViewModelProvider(this).get(OverviewViewModel.class);

        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        vAvatarImage = view.findViewById(R.id.imageOnlineStatus);
        vBotName = view.findViewById(R.id.botUserDesc);
        vCommandGraph = view.findViewById(R.id.commandGraph);
        vCommandCount = view.findViewById(R.id.commandsDesc);
        vOnlineStatus = view.findViewById(R.id.statusDesc);
        vUptime = view.findViewById(R.id.uptimeDesc);
        vLoadingFrame = view.findViewById(R.id.loadingFrame);
        vSettingsButton = view.findViewById(R.id.settingsButton);
        ReportResponse status = overviewViewModel.getStatus().getValue();
        //drawReportResponse(status);


        overviewViewModel.getStatus().observe(this.getViewLifecycleOwner(), new Observer<ReportResponse>()
        {
            @Override
            public void onChanged(ReportResponse reportResponse)
            {
                Log.d("YEET", "YEET");
                drawReportResponse(reportResponse);
            }
        });

        vSettingsButton.setOnClickListener(new View.OnClickListener()
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
    public void onPause()
    {
        super.onPause();
        pauseSecond = Instant.now().getEpochSecond();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (pauseSecond > 0 && (pauseSecond / 60) < (Instant.now().getEpochSecond() / 60)) //TODO: Maybe remove
        {
            //status = null;
            //new ConnectionTask().execute();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        outState.putSerializable(PAUSE_TIME_TAG, pauseSecond);
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

    private void drawReportResponse(ReportResponse status)
    {
        if (status != null && status.isOnline())
        {
            Picasso.get().load(status.getProfileImageURL()).into(vAvatarImage);
            vBotName.setText(status.getBotName());
            vOnlineStatus.setText("Online");
            vCommandCount.setText(""+status.getCommandUsageForSession());
            vUptime.setText(formatSecondsToddHHmmss(status.getUptimeSeconds()));
            drawCommandGraph(status);
        }
        else
        {
            vAvatarImage.setImageResource(R.drawable.ic_cloud_off_black_24dp);
            vBotName.setText("Unknown");
            vOnlineStatus.setText("Offline");
            vCommandCount.setText("N/A");
            vUptime.setText("N/A");
            drawCommandGraph(status);
        }

    }

    private void drawCommandGraph(ReportResponse status)
    {
        vCommandGraph.removeAllSeries();
        LineGraphSeries<DataPoint> series = null;
        int hoursSinceEpoch = (int) Math.floor(Instant.now().getEpochSecond()/3600.0);
        int hourOfDay = hoursSinceEpoch % 24;
        if (status != null && status.isOnline())
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
            series = new LineGraphSeries<>(dataPoints);
            vCommandGraph.addSeries(series);
        }
        vCommandGraph.getViewport().setMaxX(hourOfDay);
        vCommandGraph.getViewport().setMinX(hourOfDay-23);
        vCommandGraph.getViewport().setMinY(0);
        vCommandGraph.getViewport().setXAxisBoundsManual(true);
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
