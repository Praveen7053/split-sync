package com.splitsync.presentation;

import android.app.Activity;
import android.widget.Toast;

import com.splitsync.R;
import com.splitsync.SplitSyncApp;
import com.splitsync.application.ApplicationServices;

/**
 * Small UI helpers shared by activities.
 */
public final class UiHelpers {

    private UiHelpers() {
    }

    public static ApplicationServices services(Activity activity) {
        return ((SplitSyncApp) activity.getApplication()).services();
    }

    public static void showError(Activity activity, Throwable error) {
        String message = error.getMessage();
        if (message == null || message.isBlank()) {
            message = activity.getString(R.string.error_generic);
        }
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }
}
