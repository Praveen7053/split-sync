package com.splitsync.presentation;

import android.app.Activity;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.splitsync.R;
import com.splitsync.SplitSyncApp;
import com.splitsync.application.ApplicationServices;
import com.splitsync.domain.valueobject.Money;

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
        showMessage(activity, message);
    }

    public static void showError(Activity activity, String message) {
        showMessage(activity, message == null || message.isBlank()
                ? activity.getString(R.string.error_generic)
                : message);
    }

    public static void showSuccess(Activity activity, int messageRes) {
        showMessage(activity, activity.getString(messageRes));
    }

    public static void showMessage(Activity activity, String message) {
        View root = activity.findViewById(android.R.id.content);
        if (root != null) {
            Snackbar.make(root, message, Snackbar.LENGTH_LONG).show();
        }
    }

    public static void setSaving(MaterialButton button, boolean saving, CharSequence idleLabel) {
        button.setEnabled(!saving);
        button.setAlpha(saving ? 0.6f : 1f);
        button.setText(saving ? button.getContext().getString(R.string.saving) : idleLabel);
    }

    public static String formatMoney(Money money) {
        return money == null ? "0.00" : money.toString();
    }

    public static String formatBalanceLine(Activity activity, String name, Money net) {
        if (net == null || net.isZero()) {
            return activity.getString(R.string.balance_settled, name);
        }
        if (net.isPositive()) {
            return activity.getString(R.string.balance_owed, name, formatMoney(net));
        }
        return activity.getString(R.string.balance_owes, name, formatMoney(net.negate()));
    }
}
