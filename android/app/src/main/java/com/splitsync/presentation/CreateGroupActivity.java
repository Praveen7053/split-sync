package com.splitsync.presentation;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.button.MaterialButton;
import com.splitsync.R;
import com.splitsync.domain.type.GroupType;

public class CreateGroupActivity extends SplitSyncActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        setTitle(R.string.create_group);

        EditText inputGroupName = findViewById(R.id.inputGroupName);
        EditText inputCurrency = findViewById(R.id.inputCurrency);
        Spinner spinnerGroupType = findViewById(R.id.spinnerGroupType);
        MaterialButton buttonSaveGroup = findViewById(R.id.buttonSaveGroup);
        CharSequence idleLabel = buttonSaveGroup.getText();

        spinnerGroupType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, GroupType.values()));

        buttonSaveGroup.setOnClickListener(v -> {
            String name = textOf(inputGroupName);
            String currency = textOf(inputCurrency);
            GroupType type = (GroupType) spinnerGroupType.getSelectedItem();
            boolean valid = true;
            if (name.isEmpty()) {
                inputGroupName.setError(getString(R.string.error_required));
                inputGroupName.requestFocus();
                valid = false;
            } else {
                inputGroupName.setError(null);
            }
            if (currency.isEmpty()) {
                inputCurrency.setError(getString(R.string.error_required));
                if (valid) {
                    inputCurrency.requestFocus();
                }
                valid = false;
            } else {
                inputCurrency.setError(null);
            }
            if (!valid) {
                return;
            }

            UiHelpers.setSaving(buttonSaveGroup, true, idleLabel);
            AppExecutors.io(() -> {
                try {
                    UiHelpers.services(this).groups().createGroup(name, type, currency);
                    runOnUiThread(() -> {
                        UiHelpers.showSuccess(this, R.string.saved_group);
                        finish();
                    });
                } catch (Exception error) {
                    runOnUiThread(() -> {
                        UiHelpers.setSaving(buttonSaveGroup, false, idleLabel);
                        UiHelpers.showError(this, error);
                    });
                }
            });
        });
    }

    private static String textOf(EditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString().trim();
    }
}
