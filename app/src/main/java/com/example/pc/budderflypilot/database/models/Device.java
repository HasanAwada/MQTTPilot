package com.example.pc.budderflypilot.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Hasan.Awada on 2/9/2018.
 */

@DatabaseTable(tableName = "devices")
public class Device {

    public static String ON = "ON";
    public static String OFF = "OFF";
    public static String UN_KNOWN = "UN_KNOWN";

    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String macAddress;

    @DatabaseField
    private String status;

    @DatabaseField
    private Double powerConsump;

    public Device() {

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getPowerConsump() {
        return powerConsump;
    }

    public void setPowerConsump(Double powerConsump) {
        this.powerConsump = powerConsump;
    }
}
