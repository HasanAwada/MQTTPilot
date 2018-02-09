package com.example.pc.budderflypilot.database.repositories;

import android.content.Context;

import com.example.pc.budderflypilot.database.DatabaseHelper;
import com.example.pc.budderflypilot.database.DatabaseManager;
import com.example.pc.budderflypilot.database.models.Device;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Hasan.Awada on 2/9/2018.
 */

public class DeviceRepository {

    private Dao<Device, Integer> deviceDao;

    public DeviceRepository(Context ctx) {
        try {
            DatabaseManager dbManager = new DatabaseManager();
            DatabaseHelper db = dbManager.getHelper(ctx);
            deviceDao = db.getDeviceDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Device find(int deviceId) {
        try {
            return deviceDao.queryBuilder().where().eq("id", deviceId).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int create(Device device) {
        try {
            return deviceDao.create(device);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int update(Device device) {
        try {
            return deviceDao.update(device);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(Device device) {
        try {
            return deviceDao.delete(device);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Device> getAll() {
        try {
            return deviceDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
