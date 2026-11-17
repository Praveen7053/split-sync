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
import com.splitsync.domain.model.Expense;
import com.splitsync.domain.valueobject.GroupId;

import java.util.ArrayList;
import java.util.List;

public class ExpenseListActivity extends AppCompatActivity {

    public static final String EXTRA_EXPENSE_ID = "expenseId";

    private GroupId groupId;
    private final List<Expense> expenses = new ArrayList<>();
    private ArrayAdapter<String> adapter;

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
        AppExecutors.io(() -> {
            try {
                List<Expense> loaded = UiHelpers.services(this).expenses().listExpenses(groupId);
                List<String> rows = new ArrayList<>();
                for (Expense expense : loaded) {
                    String payer = UiHelpers.services(this).profiles().displayNameOf(expense.paidBy());
                    rows.add(expense.description() + " · " + expense.amount() + " · " + payer);
                }
                runOnUiThread(() -> {
                    expenses.clear();
                    expenses.addAll(loaded);
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
