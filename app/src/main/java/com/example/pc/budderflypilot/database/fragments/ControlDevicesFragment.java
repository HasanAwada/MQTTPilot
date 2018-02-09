package com.example.pc.budderflypilot.database.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.budderflypilot.R;


public class ControlDevicesFragment extends Fragment {

    public ControlDevicesFragment() {
        // Required empty public constructor
    }


    public static ControlDevicesFragment newInstance(String param1, String param2) {
        ControlDevicesFragment fragment = new ControlDevicesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_control_devices, container, false);
    }

}
