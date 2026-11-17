package com.splitsync.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.splitsync.R;
import com.splitsync.domain.model.Expense;
import com.splitsync.domain.model.Membership;
import com.splitsync.domain.type.SplitType;
import com.splitsync.domain.validation.SplitParticipantInput;
import com.splitsync.domain.valueobject.ExpenseId;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.Money;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddEditExpenseActivity extends AppCompatActivity {

    private GroupId groupId;
    private ExpenseId expenseId;
    private final List<MemberOption> members = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_expense);

        groupId = GroupId.of(getIntent().getStringExtra(GroupDetailActivity.EXTRA_GROUP_ID));
        String expenseIdValue = getIntent().getStringExtra(ExpenseListActivity.EXTRA_EXPENSE_ID);
        boolean editing = expenseIdValue != null && !expenseIdValue.isBlank();
        if (editing) {
            expenseId = ExpenseId.of(expenseIdValue);
            setTitle(R.string.edit_expense);
        } else {
            setTitle(R.string.add_expense);
        }

        EditText inputDescription = findViewById(R.id.inputDescription);
        EditText inputAmount = findViewById(R.id.inputAmount);
        Spinner spinnerPaidBy = findViewById(R.id.spinnerPaidBy);
        MaterialButton buttonSaveExpense = findViewById(R.id.buttonSaveExpense);
        MaterialButton buttonDeleteExpense = findViewById(R.id.buttonDeleteExpense);
        buttonDeleteExpense.setVisibility(editing ? View.VISIBLE : View.GONE);

        AppExecutors.io(() -> {
            try {
                List<Membership> memberships = UiHelpers.services(this).groups().listMembers(groupId);
                List<MemberOption> options = new ArrayList<>();
                for (Membership membership : memberships) {
                    options.add(new MemberOption(membership.userId(), UiHelpers.services(this).profiles().displayNameOf(membership.userId())));
                }
                Expense existing = editing ? UiHelpers.services(this).expenses().getExpense(expenseId) : null;
                runOnUiThread(() -> {
                    members.clear();
                    members.addAll(options);
                    spinnerPaidBy.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, members));
                    if (existing != null) {
                        inputDescription.setText(existing.description());
                        inputAmount.setText(existing.amount().toString());
                        for (int index = 0; index < members.size(); index++) {
                            if (members.get(index).userId().equals(existing.paidBy())) {
                                spinnerPaidBy.setSelection(index);
                                break;
                            }
                        }
                    }
                });
            } catch (Exception error) {
                runOnUiThread(() -> UiHelpers.showError(this, error));
            }
        });

        buttonSaveExpense.setOnClickListener(v -> {
            String description = textOf(inputDescription);
            String amountText = textOf(inputAmount);
            MemberOption paidBy = (MemberOption) spinnerPaidBy.getSelectedItem();
            if (paidBy == null) {
                UiHelpers.showError(this, new IllegalStateException("Select a payer"));
                return;
            }
            AppExecutors.io(() -> {
                try {
                    List<SplitParticipantInput> participants = new ArrayList<>();
                    for (MemberOption member : members) {
                        participants.add(SplitParticipantInput.equalShare(member.userId()));
                    }
                    if (editing) {
                        UiHelpers.services(this).expenses().updateExpense(
                                expenseId,
                                description,
                                Money.of(amountText),
                                paidBy.userId(),
                                LocalDate.now(),
                                SplitType.EQUAL,
                                participants);
                    } else {
                        UiHelpers.services(this).expenses().addExpense(
                                groupId,
                                description,
                                Money.of(amountText),
                                paidBy.userId(),
                                LocalDate.now(),
                                SplitType.EQUAL,
                                participants);
                    }
                    runOnUiThread(this::finish);
                } catch (Exception error) {
                    runOnUiThread(() -> UiHelpers.showError(this, error));
                }
            });
        });

        buttonDeleteExpense.setOnClickListener(v -> AppExecutors.io(() -> {
            try {
                UiHelpers.services(this).expenses().deleteExpense(expenseId);
                runOnUiThread(this::finish);
            } catch (Exception error) {
                runOnUiThread(() -> UiHelpers.showError(this, error));
            }
        }));
    }

    private static String textOf(EditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString().trim();
    }
}
