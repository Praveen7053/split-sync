package com.splitsync.presentation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Single background executor for Room/service calls off the UI thread.
 */
public final class AppExecutors {

    private static final ExecutorService IO = Executors.newSingleThreadExecutor();

    private AppExecutors() {
    }

    public static void io(Runnable runnable) {
        IO.execute(runnable);
    }
}
