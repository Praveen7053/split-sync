package com.splitsync.presentation;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.splitsync.application.ApplicationServices;
import com.splitsync.domain.model.User;

/**
 * Launcher that routes to onboarding or the group list.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationServices services = UiHelpers.services(this);
        AppExecutors.io(() -> {
            try {
                User profile = services.profiles().findLocalProfileOrNull();
                runOnUiThread(() -> {
                    if (profile == null) {
                        startActivity(new Intent(this, CreateProfileActivity.class));
                    } else {
                        startActivity(new Intent(this, GroupListActivity.class));
                    }
                    finish();
                });
            } catch (Exception error) {
                runOnUiThread(() -> UiHelpers.showError(this, error));
            }
        });
    }
}
