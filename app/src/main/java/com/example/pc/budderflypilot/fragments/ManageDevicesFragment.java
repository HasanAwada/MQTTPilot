package com.example.pc.budderflypilot.fragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.pc.budderflypilot.MainActivity;
import com.example.pc.budderflypilot.R;
import com.example.pc.budderflypilot.adapters.DeviceAdapter;
import com.example.pc.budderflypilot.database.models.Device;
import com.example.pc.budderflypilot.database.repositories.DeviceRepository;


import java.util.List;


public class ManageDevicesFragment extends Fragment {

    public static String TAG = ManageDevicesFragment.class.getSimpleName();

    private MainActivity mainActivity;
    private View mView;

    private RecyclerView recyclerView;
    private List<Device> devicesList;
    private DeviceAdapter deviceAdapter;


    public ManageDevicesFragment() {
        // Required empty public constructor
    }


    public static ManageDevicesFragment newInstance() {
        ManageDevicesFragment fragment = new ManageDevicesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        MainActivity.pageIndex = 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mView == null)
            mView = inflater.inflate(R.layout.fragment_manage_devices, container, false);
        initViews();
        return mView;
    }

    private void initViews() {
        mView.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etMacAddress = (EditText) mView.findViewById(R.id.etMacAddress);
                String macAddress = etMacAddress.getText() != null ? etMacAddress.getText().toString() : "";
                Device device = new Device();
                device.setMacAddress(macAddress);
                (new DeviceRepository(mainActivity)).create(device);
                etMacAddress.setText("");
            }
        });
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.mRecyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        refreshView();
    }

    private void refreshView() {
        devicesList = (new DeviceRepository(mainActivity)).getAll();
        if (devicesList != null && devicesList.size() > 0) {
            deviceAdapter = new DeviceAdapter(mainActivity, devicesList);
            recyclerView.setAdapter(deviceAdapter);
        }
    }
}
