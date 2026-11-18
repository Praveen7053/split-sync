package com.splitsync.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.splitsync.R;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.MemberBalance;

import java.util.ArrayList;
import java.util.List;

public class BalancesActivity extends SplitSyncActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list);
        setTitle(R.string.balances);

        GroupId groupId = GroupId.of(getIntent().getStringExtra(GroupDetailActivity.EXTRA_GROUP_ID));
        ListView listItems = findViewById(R.id.listItems);
        TextView textEmpty = findViewById(R.id.textEmpty);
        ProgressBar progressLoading = findViewById(R.id.progressLoading);
        MaterialButton buttonPrimary = findViewById(R.id.buttonPrimary);

        textEmpty.setText(R.string.empty_balances);
        buttonPrimary.setVisibility(View.GONE);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listItems.setAdapter(adapter);

        progressLoading.setVisibility(View.VISIBLE);
        AppExecutors.io(() -> {
            try {
                List<MemberBalance> balances = UiHelpers.services(this).balances().getGroupBalances(groupId);
                List<String> rows = new ArrayList<>();
                for (MemberBalance balance : balances) {
                    String name = UiHelpers.services(this).profiles().displayNameOf(balance.userId());
                    rows.add(UiHelpers.formatBalanceLine(this, name, balance.netAmount()));
                }
                runOnUiThread(() -> {
                    progressLoading.setVisibility(View.GONE);
                    adapter.clear();
                    adapter.addAll(rows);
                    adapter.notifyDataSetChanged();
                    textEmpty.setVisibility(rows.isEmpty() ? View.VISIBLE : View.GONE);
                });
            } catch (Exception error) {
                runOnUiThread(() -> {
                    progressLoading.setVisibility(View.GONE);
                    UiHelpers.showError(this, error);
                });
            }
        });
    }
}
