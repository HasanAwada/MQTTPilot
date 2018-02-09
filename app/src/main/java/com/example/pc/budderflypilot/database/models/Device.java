package com.example.pc.budderflypilot.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Hasan.Awada on 2/9/2018.
 */

@DatabaseTable(tableName = "devices")
public class Device {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String macAddress;

    public Device() {

    }

    public Device(int id, String macAddress) {
        this.id = id;
        this.macAddress = macAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
