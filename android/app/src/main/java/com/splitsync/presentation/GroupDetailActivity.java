package com.splitsync.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.splitsync.R;
import com.splitsync.domain.model.Expense;
import com.splitsync.domain.model.Group;
import com.splitsync.domain.model.Membership;
import com.splitsync.domain.model.User;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.Money;

import java.util.List;

public class GroupDetailActivity extends SplitSyncActivity {

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
        TextView textGroupSummary = findViewById(R.id.textGroupSummary);
        TextView textMembers = findViewById(R.id.textMembers);

        AppExecutors.io(() -> {
            try {
                Group group = UiHelpers.services(this).groups().getGroup(groupId);
                List<Membership> memberships = UiHelpers.services(this).groups().listMembers(groupId);
                List<Expense> expenses = UiHelpers.services(this).expenses().listExpenses(groupId);
                Money total = Money.zero();
                for (Expense expense : expenses) {
                    total = total.plus(expense.amount());
                }

                StringBuilder membersText = new StringBuilder();
                for (Membership membership : memberships) {
                    User user = UiHelpers.services(this).profiles().findUserOrNull(membership.userId());
                    String name = user == null ? membership.userId().value() : user.displayName();
                    if (membersText.length() > 0) {
                        membersText.append('\n');
                    }
                    membersText.append(name).append(" (").append(membership.role().name()).append(')');
                    if (user != null && user.phoneNumber() != null && !user.phoneNumber().isBlank()) {
                        membersText.append(" · ").append(user.phoneNumber());
                    }
                }
                String members = membersText.toString();
                String summary = getString(
                        R.string.group_meta,
                        group.groupType().name(),
                        group.currency().value(),
                        memberships.size(),
                        UiHelpers.formatMoney(total));
                runOnUiThread(() -> {
                    setTitle(group.name());
                    textGroupTitle.setText(group.name());
                    textGroupMeta.setText(group.groupType().name() + " · " + group.currency().value());
                    textGroupSummary.setText(summary);
                    textMembers.setText(members.length() == 0 ? getString(R.string.members) : members);
                });
            } catch (Exception error) {
                runOnUiThread(() -> UiHelpers.showError(this, error));
            }
        });
    }
}
