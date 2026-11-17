package com.splitsync.presentation;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.splitsync.R;

public class CreateProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        setTitle(R.string.create_profile_title);

        EditText inputDisplayName = findViewById(R.id.inputDisplayName);
        EditText inputDeviceName = findViewById(R.id.inputDeviceName);
        MaterialButton buttonContinue = findViewById(R.id.buttonContinue);

        String defaultDevice = Build.MODEL == null || Build.MODEL.isBlank() ? "Android Device" : Build.MODEL;
        inputDeviceName.setText(defaultDevice);

        buttonContinue.setOnClickListener(v -> {
            String displayName = textOf(inputDisplayName);
            String deviceName = textOf(inputDeviceName);
            AppExecutors.io(() -> {
                try {
                    UiHelpers.services(this).profiles().createLocalProfile(displayName, deviceName);
                    runOnUiThread(() -> {
                        startActivity(new Intent(this, GroupListActivity.class));
                        finish();
                    });
                } catch (Exception error) {
                    runOnUiThread(() -> UiHelpers.showError(this, error));
                }
            });
        });
    }

    private static String textOf(EditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString().trim();
    }
}
