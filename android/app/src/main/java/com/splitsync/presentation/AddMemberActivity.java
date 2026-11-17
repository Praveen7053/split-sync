package com.splitsync.presentation;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.splitsync.R;
import com.splitsync.domain.type.MembershipRole;
import com.splitsync.domain.valueobject.GroupId;

public class AddMemberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        setTitle(R.string.add_member);

        GroupId groupId = GroupId.of(getIntent().getStringExtra(GroupDetailActivity.EXTRA_GROUP_ID));
        EditText inputMemberName = findViewById(R.id.inputMemberName);
        MaterialButton buttonSaveMember = findViewById(R.id.buttonSaveMember);

        buttonSaveMember.setOnClickListener(v -> {
            String name = inputMemberName.getText() == null ? "" : inputMemberName.getText().toString().trim();
            AppExecutors.io(() -> {
                try {
                    UiHelpers.services(this).groups().addLocalMember(groupId, name, MembershipRole.MEMBER);
                    runOnUiThread(this::finish);
                } catch (Exception error) {
                    runOnUiThread(() -> UiHelpers.showError(this, error));
                }
            });
        });
    }
}
