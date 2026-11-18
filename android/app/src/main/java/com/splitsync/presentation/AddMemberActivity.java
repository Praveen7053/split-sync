package com.splitsync.presentation;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.splitsync.R;
import com.splitsync.domain.type.MembershipRole;
import com.splitsync.domain.valueobject.GroupId;

public class AddMemberActivity extends SplitSyncActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        setTitle(R.string.add_member);

        GroupId groupId = GroupId.of(getIntent().getStringExtra(GroupDetailActivity.EXTRA_GROUP_ID));
        EditText inputMemberName = findViewById(R.id.inputMemberName);
        EditText inputMemberPhone = findViewById(R.id.inputMemberPhone);
        MaterialButton buttonSaveMember = findViewById(R.id.buttonSaveMember);
        CharSequence idleLabel = buttonSaveMember.getText();

        buttonSaveMember.setOnClickListener(v -> {
            String name = textOf(inputMemberName);
            String phone = textOf(inputMemberPhone);
            if (name.isEmpty()) {
                inputMemberName.setError(getString(R.string.error_required));
                inputMemberName.requestFocus();
                return;
            }
            inputMemberName.setError(null);

            UiHelpers.setSaving(buttonSaveMember, true, idleLabel);
            AppExecutors.io(() -> {
                try {
                    UiHelpers.services(this).groups().addLocalMember(groupId, name, phone, MembershipRole.MEMBER);
                    runOnUiThread(() -> {
                        UiHelpers.showSuccess(this, R.string.saved_member);
                        finish();
                    });
                } catch (Exception error) {
                    runOnUiThread(() -> {
                        UiHelpers.setSaving(buttonSaveMember, false, idleLabel);
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
