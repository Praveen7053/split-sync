package com.splitsync.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.splitsync.R;
import com.splitsync.domain.model.Group;
import com.splitsync.domain.model.Membership;
import com.splitsync.domain.valueobject.GroupId;

import java.util.List;

public class GroupDetailActivity extends AppCompatActivity {

    public static final String EXTRA_GROUP_ID = "groupId";

    private GroupId groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        groupId = GroupId.of(getIntent().getStringExtra(EXTRA_GROUP_ID));

        findViewById(R.id.buttonAddMember).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddMemberActivity.class);
            intent.putExtra(EXTRA_GROUP_ID, groupId.value());
            startActivity(intent);
        });
        findViewById(R.id.buttonExpenses).setOnClickListener(v -> {
            Intent intent = new Intent(this, ExpenseListActivity.class);
            intent.putExtra(EXTRA_GROUP_ID, groupId.value());
            startActivity(intent);
        });
        findViewById(R.id.buttonBalances).setOnClickListener(v -> {
            Intent intent = new Intent(this, BalancesActivity.class);
            intent.putExtra(EXTRA_GROUP_ID, groupId.value());
            startActivity(intent);
        });
        findViewById(R.id.buttonSettlements).setOnClickListener(v -> {
            Intent intent = new Intent(this, SettlementListActivity.class);
            intent.putExtra(EXTRA_GROUP_ID, groupId.value());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        TextView textGroupTitle = findViewById(R.id.textGroupTitle);
        TextView textGroupMeta = findViewById(R.id.textGroupMeta);
        TextView textMembers = findViewById(R.id.textMembers);

        AppExecutors.io(() -> {
            try {
                Group group = UiHelpers.services(this).groups().getGroup(groupId);
                List<Membership> memberships = UiHelpers.services(this).groups().listMembers(groupId);
                StringBuilder membersText = new StringBuilder();
                for (Membership membership : memberships) {
                    String name = UiHelpers.services(this).profiles().displayNameOf(membership.userId());
                    if (!membersText.isEmpty()) {
                        membersText.append('\n');
                    }
                    membersText.append(name).append(" (").append(membership.role().name()).append(')');
                }
                String members = membersText.toString();
                runOnUiThread(() -> {
                    setTitle(group.name());
                    textGroupTitle.setText(group.name());
                    textGroupMeta.setText(group.groupType().name() + " · " + group.currency().value());
                    textMembers.setText(members.isEmpty() ? getString(R.string.members) : members);
                });
            } catch (Exception error) {
                runOnUiThread(() -> UiHelpers.showError(this, error));
            }
        });
    }
}
