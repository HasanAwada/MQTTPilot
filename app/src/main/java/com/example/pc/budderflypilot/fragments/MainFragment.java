package com.example.pc.budderflypilot.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.budderflypilot.MainActivity;
import com.example.pc.budderflypilot.R;


public class MainFragment extends Fragment {
    public static String TAG = MainFragment.class.getSimpleName();

    private MainActivity mainActivity;
    private View view;

    public MainFragment() {
        // Required empty public constructor
    }


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null)
            view = inflater.inflate(R.layout.fragment_main, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        view.findViewById(R.id.btnManageDevices).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container, ManageDevicesFragment.newInstance(), MainFragment.class.getSimpleName()).commit();
            }
        });
    }

}
