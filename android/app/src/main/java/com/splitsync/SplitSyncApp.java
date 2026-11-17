package com.splitsync;

import android.app.Application;

import com.splitsync.application.ApplicationServices;
import com.splitsync.data.local.database.DatabaseProvider;
import com.splitsync.data.local.database.SplitSyncDatabase;

/**
 * Application entry point that owns Room DB and application services.
 */
public class SplitSyncApp extends Application {

    private SplitSyncDatabase database;
    private ApplicationServices services;

    @Override
    public void onCreate() {
        super.onCreate();
        database = DatabaseProvider.get(this);
        services = new ApplicationServices(database);
    }

    public SplitSyncDatabase database() {
        return database;
    }

    public ApplicationServices services() {
        return services;
    }
}
