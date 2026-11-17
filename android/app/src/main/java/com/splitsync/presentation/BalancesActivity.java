package com.splitsync.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.splitsync.R;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.MemberBalance;

import java.util.ArrayList;
import java.util.List;

public class BalancesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list);
        setTitle(R.string.balances);

        GroupId groupId = GroupId.of(getIntent().getStringExtra(GroupDetailActivity.EXTRA_GROUP_ID));
        ListView listItems = findViewById(R.id.listItems);
        TextView textEmpty = findViewById(R.id.textEmpty);
        MaterialButton buttonPrimary = findViewById(R.id.buttonPrimary);

        textEmpty.setText(R.string.empty_balances);
        buttonPrimary.setVisibility(View.GONE);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listItems.setAdapter(adapter);

        AppExecutors.io(() -> {
            try {
                List<MemberBalance> balances = UiHelpers.services(this).balances().getGroupBalances(groupId);
                List<String> rows = new ArrayList<>();
                for (MemberBalance balance : balances) {
                    String name = UiHelpers.services(this).profiles().displayNameOf(balance.userId());
                    rows.add(name + " · " + balance.netAmount());
                }
                runOnUiThread(() -> {
                    adapter.clear();
                    adapter.addAll(rows);
                    adapter.notifyDataSetChanged();
                    textEmpty.setVisibility(rows.isEmpty() ? View.VISIBLE : View.GONE);
                });
            } catch (Exception error) {
                runOnUiThread(() -> UiHelpers.showError(this, error));
            }
        });
    }
}
