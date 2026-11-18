package com.splitsync.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.splitsync.R;
import com.splitsync.domain.model.Expense;
import com.splitsync.domain.model.Membership;
import com.splitsync.domain.valueobject.GroupId;

import java.util.ArrayList;
import java.util.List;

public class ExpenseListActivity extends SplitSyncActivity {

    public static final String EXTRA_EXPENSE_ID = "expenseId";

    private GroupId groupId;
    private final List<Expense> expenses = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private int memberCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list);
        setTitle(R.string.expenses);

        groupId = GroupId.of(getIntent().getStringExtra(GroupDetailActivity.EXTRA_GROUP_ID));
        ListView listItems = findViewById(R.id.listItems);
        TextView textEmpty = findViewById(R.id.textEmpty);
        MaterialButton buttonPrimary = findViewById(R.id.buttonPrimary);

        textEmpty.setText(R.string.empty_expenses);
        buttonPrimary.setText(R.string.add_expense);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listItems.setAdapter(adapter);

        listItems.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, AddEditExpenseActivity.class);
            intent.putExtra(GroupDetailActivity.EXTRA_GROUP_ID, groupId.value());
            intent.putExtra(EXTRA_EXPENSE_ID, expenses.get(position).expenseId().value());
            startActivity(intent);
        });

        buttonPrimary.setOnClickListener(v -> {
            if (memberCount < 2) {
                UiHelpers.showError(this, getString(R.string.error_need_two_members));
                return;
            }
            Intent intent = new Intent(this, AddEditExpenseActivity.class);
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
        TextView textEmpty = findViewById(R.id.textEmpty);
        ProgressBar progressLoading = findViewById(R.id.progressLoading);
        progressLoading.setVisibility(View.VISIBLE);
        AppExecutors.io(() -> {
            try {
                List<Membership> memberships = UiHelpers.services(this).groups().listMembers(groupId);
                List<Expense> loaded = UiHelpers.services(this).expenses().listExpenses(groupId);
                List<String> rows = new ArrayList<>();
                for (Expense expense : loaded) {
                    String payer = UiHelpers.services(this).profiles().displayNameOf(expense.paidBy());
                    rows.add(getString(
                            R.string.expense_row,
                            expense.description(),
                            UiHelpers.formatMoney(expense.amount()),
                            payer,
                            expense.expenseDate().toString()));
                }
                runOnUiThread(() -> {
                    progressLoading.setVisibility(View.GONE);
                    memberCount = memberships.size();
                    expenses.clear();
                    expenses.addAll(loaded);
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
