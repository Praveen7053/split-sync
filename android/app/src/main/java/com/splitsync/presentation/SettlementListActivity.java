package com.splitsync.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.splitsync.R;
import com.splitsync.domain.model.Settlement;
import com.splitsync.domain.valueobject.GroupId;

import java.util.ArrayList;
import java.util.List;

public class SettlementListActivity extends AppCompatActivity {

    private GroupId groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list);
        setTitle(R.string.settlements);

        groupId = GroupId.of(getIntent().getStringExtra(GroupDetailActivity.EXTRA_GROUP_ID));
        MaterialButton buttonPrimary = findViewById(R.id.buttonPrimary);
        buttonPrimary.setText(R.string.add_settlement);
        buttonPrimary.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddSettlementActivity.class);
            intent.putExtra(GroupDetailActivity.EXTRA_GROUP_ID, groupId.value());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        ListView listItems = findViewById(R.id.listItems);
        TextView textEmpty = findViewById(R.id.textEmpty);
        textEmpty.setText(R.string.empty_settlements);

        AppExecutors.io(() -> {
            try {
                List<Settlement> settlements = UiHelpers.services(this).settlements().listSettlements(groupId);
                List<String> rows = new ArrayList<>();
                for (Settlement settlement : settlements) {
                    String from = UiHelpers.services(this).profiles().displayNameOf(settlement.paidBy());
                    String to = UiHelpers.services(this).profiles().displayNameOf(settlement.paidTo());
                    rows.add(from + " → " + to + " · " + settlement.amount());
                }
                runOnUiThread(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rows);
                    listItems.setAdapter(adapter);
                    textEmpty.setVisibility(rows.isEmpty() ? View.VISIBLE : View.GONE);
                });
            } catch (Exception error) {
                runOnUiThread(() -> UiHelpers.showError(this, error));
            }
        });
    }
}
