package com.splitsync;

import android.app.Application;

import com.splitsync.data.local.database.DatabaseProvider;
import com.splitsync.data.local.database.SplitSyncDatabase;

/**
 * Application entry point that owns the local Room database singleton.
 */
public class SplitSyncApp extends Application {

    private SplitSyncDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = DatabaseProvider.get(this);
    }

    public SplitSyncDatabase database() {
        return database;
    }
}
