package com.example.pc.budderflypilot.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.pc.budderflypilot.database.models.Device;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Pc on 2/9/2018.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "WataniaApp.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 9;

    // the DAO object we use to access the Data table
    private Dao<Device, Integer> deviceDao = null;

    // the runtime exception DAO object we use to access the Data table
    private RuntimeExceptionDao<Device, Integer> deviceRuntimeDao = null;

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");

            TableUtils.createTable(connectionSource, Device.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
        Log.i(DatabaseHelper.class.getName(), "created new entries in onCreate");
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            if (oldVersion == 1) {
                TableUtils.dropTable(connectionSource, Device.class, true);

                onCreate(db, connectionSource);
            }

            Log.d(TAG, "onUpgrade: from database version ==> " + oldVersion);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }


    public void ClearAll() {
        try {
            TableUtils.clearTable(connectionSource, Device.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        deviceDao = null;
    }

    /**
     * Returns the Database Access Object (DAO) for our Data class. It will create it or just give the cached
     * value.
     */

    public Dao<Device, Integer> getDeviceDao() throws SQLException {
        if (deviceDao == null) {
            deviceDao = getDao(Device.class);
        }
        return deviceDao;
    }


    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our Data class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */


    public RuntimeExceptionDao<Device, Integer> getDeviceRuntimeDao() {
        if (deviceRuntimeDao == null) {
            deviceRuntimeDao = getRuntimeExceptionDao(Device.class);
        }
        return deviceRuntimeDao;
    }


}