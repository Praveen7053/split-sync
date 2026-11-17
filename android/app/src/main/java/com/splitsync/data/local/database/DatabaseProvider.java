package com.splitsync.data.local.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;

/**
 * Provides a single Room database instance for the app process.
 */
public final class DatabaseProvider {

    private static volatile SplitSyncDatabase instance;

    private DatabaseProvider() {
    }

    @NonNull
    public static SplitSyncDatabase get(@NonNull Context context) {
        if (instance == null) {
            synchronized (DatabaseProvider.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), SplitSyncDatabase.class, SplitSyncDatabase.NAME)
                            // V1 schema starts at version 1; add Migration objects here as schema evolves.
                            .addMigrations()
                            .build();
                }
            }
        }
        return instance;
    }

    /**
     * Test helper to replace the singleton between unit tests.
     */
    public static void setForTests(SplitSyncDatabase database) {
        instance = database;
    }
}
