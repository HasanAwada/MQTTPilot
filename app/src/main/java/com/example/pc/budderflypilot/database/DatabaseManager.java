package com.example.pc.budderflypilot.database;

import android.content.Context;

import com.example.pc.budderflypilot.database.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by Hasan.Awada on 2/9/2018.
 */

public class DatabaseManager {
    private DatabaseHelper databaseHelper = null;

    //gets a helper once one is created ensures it doesn't create a new one
    public DatabaseHelper getHelper(Context context) {
        if (databaseHelper == null)
            databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);

        return databaseHelper;
    }

    //releases the helper once usages has ended
    public void releaseHelper() {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}