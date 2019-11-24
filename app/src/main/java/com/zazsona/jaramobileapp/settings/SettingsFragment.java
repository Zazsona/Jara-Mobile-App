package com.zazsona.jaramobileapp.settings;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.zazsona.jaramobileapp.R;


public class SettingsFragment extends Fragment
{
    public static final String IP_ADDRESS = "com.zazsona.jaramobileapp.settings.ipaddress";
    public static final String IP_PORT = "com.zazsona.jaramobileapp.settings.ipport";
    public static final String NOTIFICATIONS_TOGGLE = "com.zazsona.jaramobileapp.settings.notiftoggle";

    private String ipAddress;
    private String ipPort;
    private boolean notificationsEnabled;

    private OnFragmentInteractionListener mListener;

    public SettingsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        EditText ipText = view.findViewById(R.id.textIPAddress);
        EditText portText = view.findViewById(R.id.textPort);
        if (getArguments() != null)
        {
            ipAddress = savedInstanceState.getString(IP_ADDRESS);
            ipPort = savedInstanceState.getString(IP_PORT);
            notificationsEnabled = savedInstanceState.getBoolean(NOTIFICATIONS_TOGGLE);
        }
        else
        {
            ipAddress = Settings.getInstance(getActivity()).getIp();
            ipText.setText(ipAddress);
            ipPort = Settings.getInstance(getContext()).getPort();
            portText.setText(ipPort);
            notificationsEnabled = Settings.getInstance(getActivity()).isNotifications();
        }

        ipText.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent)
            {
                ipAddress = ((EditText) view).getText().toString();
                return false;
            }
        });
        portText.setOnKeyListener(new View.OnKeyListener()
        {

            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent)
            {
                ipPort = ((EditText) view).getText().toString();
                return false;
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                                               + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        mListener.onIPSet(ipAddress);
        mListener.onPortSet(ipPort);
        mListener.onNotificationsToggled(notificationsEnabled);
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.getString(IP_ADDRESS, ipAddress);
        outState.getString(IP_PORT, ipPort);
        outState.getBoolean(NOTIFICATIONS_TOGGLE, notificationsEnabled);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        void onNotificationsToggled(boolean value);

        void onIPSet(String ip);

        void onPortSet(String port);
    }
}
