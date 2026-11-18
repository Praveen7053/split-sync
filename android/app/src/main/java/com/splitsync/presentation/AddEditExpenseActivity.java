package com.splitsync.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.splitsync.R;
import com.splitsync.domain.model.Expense;
import com.splitsync.domain.model.ExpenseSplit;
import com.splitsync.domain.model.Membership;
import com.splitsync.domain.model.User;
import com.splitsync.domain.type.SplitType;
import com.splitsync.domain.validation.SplitParticipantInput;
import com.splitsync.domain.valueobject.ExpenseId;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.Money;
import com.splitsync.domain.valueobject.UserId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddEditExpenseActivity extends SplitSyncActivity {

    private GroupId groupId;
    private ExpenseId expenseId;
    private boolean editing;
    private final List<MemberOption> members = new ArrayList<>();
    private final Map<UserId, EditText> splitValueInputs = new HashMap<>();
    private List<ExpenseSplit> existingSplits = List.of();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_expense);

        groupId = GroupId.of(getIntent().getStringExtra(GroupDetailActivity.EXTRA_GROUP_ID));
        String expenseIdValue = getIntent().getStringExtra(ExpenseListActivity.EXTRA_EXPENSE_ID);
        editing = expenseIdValue != null && !expenseIdValue.isBlank();
        if (editing) {
            expenseId = ExpenseId.of(expenseIdValue);
            setTitle(R.string.edit_expense);
        } else {
            setTitle(R.string.add_expense);
        }

        EditText inputDescription = findViewById(R.id.inputDescription);
        EditText inputAmount = findViewById(R.id.inputAmount);
        Spinner spinnerPaidBy = findViewById(R.id.spinnerPaidBy);
        Spinner spinnerSplitType = findViewById(R.id.spinnerSplitType);
        TextView textSplitHint = findViewById(R.id.textSplitHint);
        LinearLayout layoutSplitValues = findViewById(R.id.layoutSplitValues);
        MaterialButton buttonSaveExpense = findViewById(R.id.buttonSaveExpense);
        MaterialButton buttonDeleteExpense = findViewById(R.id.buttonDeleteExpense);
        CharSequence idleLabel = buttonSaveExpense.getText();
        buttonDeleteExpense.setVisibility(editing ? View.VISIBLE : View.GONE);

        spinnerSplitType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, SplitType.values()));
        spinnerSplitType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rebuildSplitInputs(layoutSplitValues, textSplitHint, (SplitType) spinnerSplitType.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        AppExecutors.io(() -> {
            try {
                User local = UiHelpers.services(this).profiles().getLocalProfile();
                List<Membership> memberships = UiHelpers.services(this).groups().listMembers(groupId);
                if (!editing && memberships.size() < 2) {
                    runOnUiThread(() -> {
                        UiHelpers.showError(this, getString(R.string.error_need_two_members));
                        finish();
                    });
                    return;
                }
                List<MemberOption> options = new ArrayList<>();
                for (Membership membership : memberships) {
                    options.add(new MemberOption(
                            membership.userId(),
                            UiHelpers.services(this).profiles().displayNameOf(membership.userId())));
                }
                Expense existing = editing ? UiHelpers.services(this).expenses().getExpense(expenseId) : null;
                List<ExpenseSplit> splits = editing
                        ? UiHelpers.services(this).expenses().listSplits(expenseId)
                        : List.of();
                UserId localId = local.userId();
                runOnUiThread(() -> {
                    existingSplits = splits;
                    members.clear();
                    members.addAll(options);
                    spinnerPaidBy.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, members));
                    int defaultPayer = 0;
                    for (int index = 0; index < members.size(); index++) {
                        if (members.get(index).userId().equals(localId)) {
                            defaultPayer = index;
                            break;
                        }
                    }
                    spinnerPaidBy.setSelection(defaultPayer);
                    if (existing != null) {
                        inputDescription.setText(existing.description());
                        inputAmount.setText(existing.amount().toString());
                        for (int index = 0; index < members.size(); index++) {
                            if (members.get(index).userId().equals(existing.paidBy())) {
                                spinnerPaidBy.setSelection(index);
                                break;
                            }
                        }
                        if (!splits.isEmpty()) {
                            spinnerSplitType.setSelection(splits.get(0).splitType().ordinal());
                        }
                    }
                    rebuildSplitInputs(layoutSplitValues, textSplitHint, (SplitType) spinnerSplitType.getSelectedItem());
                });
            } catch (Exception error) {
                runOnUiThread(() -> UiHelpers.showError(this, error));
            }
        });

        buttonSaveExpense.setOnClickListener(v -> {
            String description = textOf(inputDescription);
            String amountText = textOf(inputAmount);
            MemberOption paidBy = (MemberOption) spinnerPaidBy.getSelectedItem();
            SplitType splitType = (SplitType) spinnerSplitType.getSelectedItem();
            boolean valid = true;
            if (description.isEmpty()) {
                inputDescription.setError(getString(R.string.error_required));
                inputDescription.requestFocus();
                valid = false;
            } else {
                inputDescription.setError(null);
            }
            if (amountText.isEmpty()) {
                inputAmount.setError(getString(R.string.error_required));
                if (valid) {
                    inputAmount.requestFocus();
                }
                valid = false;
            } else {
                inputAmount.setError(null);
            }
            if (paidBy == null) {
                UiHelpers.showError(this, getString(R.string.error_select_payer));
                valid = false;
            }
            List<SplitParticipantInput> participants;
            try {
                participants = buildParticipants(splitType);
            } catch (IllegalArgumentException ex) {
                UiHelpers.showError(this, ex.getMessage());
                return;
            }
            if (!valid) {
                return;
            }

            UiHelpers.setSaving(buttonSaveExpense, true, idleLabel);
            AppExecutors.io(() -> {
                try {
                    if (editing) {
                        UiHelpers.services(this).expenses().updateExpense(
                                expenseId,
                                description,
                                Money.of(amountText),
                                paidBy.userId(),
                                LocalDate.now(),
                                splitType,
                                participants);
                    } else {
                        UiHelpers.services(this).expenses().addExpense(
                                groupId,
                                description,
                                Money.of(amountText),
                                paidBy.userId(),
                                LocalDate.now(),
                                splitType,
                                participants);
                    }
                    runOnUiThread(() -> {
                        UiHelpers.showSuccess(this, R.string.saved_expense);
                        finish();
                    });
                } catch (Exception error) {
                    runOnUiThread(() -> {
                        UiHelpers.setSaving(buttonSaveExpense, false, idleLabel);
                        UiHelpers.showError(this, error);
                    });
                }
            });
        });

        buttonDeleteExpense.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle(R.string.delete_expense_title)
                .setMessage(R.string.delete_expense_message)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    UiHelpers.setSaving(buttonSaveExpense, true, idleLabel);
                    AppExecutors.io(() -> {
                        try {
                            UiHelpers.services(this).expenses().deleteExpense(expenseId);
                            runOnUiThread(() -> {
                                UiHelpers.showSuccess(this, R.string.deleted_expense);
                                finish();
                            });
                        } catch (Exception error) {
                            runOnUiThread(() -> {
                                UiHelpers.setSaving(buttonSaveExpense, false, idleLabel);
                                UiHelpers.showError(this, error);
                            });
                        }
                    });
                })
                .show());
    }

    private void rebuildSplitInputs(LinearLayout layoutSplitValues, TextView textSplitHint, SplitType splitType) {
        layoutSplitValues.removeAllViews();
        splitValueInputs.clear();
        if (splitType == SplitType.EQUAL || members.isEmpty()) {
            textSplitHint.setVisibility(View.VISIBLE);
            textSplitHint.setText(R.string.split_equal_hint);
            layoutSplitValues.setVisibility(View.GONE);
            return;
        }
        textSplitHint.setVisibility(View.GONE);
        layoutSplitValues.setVisibility(View.VISIBLE);
        for (MemberOption member : members) {
            TextInputLayout inputLayout = new TextInputLayout(this);
            inputLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            inputLayout.setHint(getString(R.string.split_value_hint, member.toString()));
            TextInputEditText editText = new TextInputEditText(this);
            editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER
                    | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
            inputLayout.addView(editText);
            layoutSplitValues.addView(inputLayout);
            splitValueInputs.put(member.userId(), editText);
        }
        prefillSplitValues();
    }

    private void prefillSplitValues() {
        if (existingSplits == null || existingSplits.isEmpty()) {
            return;
        }
        for (ExpenseSplit split : existingSplits) {
            EditText input = splitValueInputs.get(split.userId());
            if (input != null && split.shareValue() != null) {
                input.setText(split.shareValue().stripTrailingZeros().toPlainString());
            }
        }
    }

    private List<SplitParticipantInput> buildParticipants(SplitType splitType) {
        List<SplitParticipantInput> participants = new ArrayList<>();
        if (splitType == SplitType.EQUAL) {
            for (MemberOption member : members) {
                participants.add(SplitParticipantInput.equalShare(member.userId()));
            }
            return participants;
        }
        for (MemberOption member : members) {
            EditText input = splitValueInputs.get(member.userId());
            String raw = input == null ? "" : textOf(input);
            if (raw.isEmpty()) {
                throw new IllegalArgumentException(getString(R.string.error_required) + ": " + member);
            }
            BigDecimal value;
            try {
                value = new BigDecimal(raw);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException(getString(R.string.error_required) + ": " + member);
            }
            switch (splitType) {
                case EXACT -> participants.add(SplitParticipantInput.exact(member.userId(), value));
                case PERCENTAGE -> participants.add(SplitParticipantInput.percentage(member.userId(), value));
                case SHARES -> participants.add(SplitParticipantInput.shares(member.userId(), value));
                default -> participants.add(SplitParticipantInput.equalShare(member.userId()));
            }
        }
        return participants;
    }

    private static String textOf(EditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString().trim();
    }
}
