package com.example.pc.budderflypilot.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pc.budderflypilot.MainActivity;
import com.example.pc.budderflypilot.R;
import com.example.pc.budderflypilot.database.models.Device;
import com.example.pc.budderflypilot.fragments.ControlDevicesFragment;
import com.example.pc.budderflypilot.fragments.MainFragment;

import java.util.List;

/**
 * Created by Hasan.Awada on 2/9/2018.
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    private List<Device> devices;
    private MainActivity mainActivity;

    public DeviceAdapter(Context context, List<Device> devices) {
        this.devices = devices;
        this.mainActivity = (MainActivity) context;
    }

    @Override
    public DeviceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_device_row, parent, false);
        return new DeviceAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final DeviceAdapter.ViewHolder holder, final int position) {
        final Device device = devices.get(position);

        if (device != null) {
            holder.tvDeviceId.setText("" + device.getId());
            holder.tvMacAddress.setText(device.getMacAddress());
            holder.btnControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container, ControlDevicesFragment.newInstance(device.getId()), MainFragment.class.getSimpleName()).commit();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (devices != null ? devices.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDeviceId;
        TextView tvMacAddress;
        Button btnControl;

        public ViewHolder(View v) {
            super(v);
            tvDeviceId = (TextView) v.findViewById(R.id.tvDeviceId);
            tvMacAddress = (TextView) v.findViewById(R.id.tvMacAddress);
            btnControl = (Button) v.findViewById(R.id.btnControl);
        }
    }
}