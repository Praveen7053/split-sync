package com.splitsync.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.splitsync.R;
import com.splitsync.domain.model.Membership;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.MemberBalance;
import com.splitsync.domain.valueobject.Money;
import com.splitsync.domain.valueobject.UserId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddSettlementActivity extends SplitSyncActivity {

    private final List<MemberOption> members = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_settlement);
        setTitle(R.string.add_settlement);

        GroupId groupId = GroupId.of(getIntent().getStringExtra(GroupDetailActivity.EXTRA_GROUP_ID));
        Spinner spinnerPaidBy = findViewById(R.id.spinnerPaidBy);
        Spinner spinnerPaidTo = findViewById(R.id.spinnerPaidTo);
        EditText inputAmount = findViewById(R.id.inputAmount);
        TextView textSettlementHint = findViewById(R.id.textSettlementHint);
        MaterialButton buttonSaveSettlement = findViewById(R.id.buttonSaveSettlement);
        CharSequence idleLabel = buttonSaveSettlement.getText();

        AppExecutors.io(() -> {
            try {
                List<Membership> memberships = UiHelpers.services(this).groups().listMembers(groupId);
                List<MemberBalance> balances = UiHelpers.services(this).balances().getGroupBalances(groupId);
                List<MemberOption> options = new ArrayList<>();
                for (Membership membership : memberships) {
                    options.add(new MemberOption(
                            membership.userId(),
                            UiHelpers.services(this).profiles().displayNameOf(membership.userId())));
                }

                MemberBalance debtor = null;
                MemberBalance creditor = null;
                for (MemberBalance balance : balances) {
                    if (balance.netAmount().isNegative() && (debtor == null
                            || balance.netAmount().compareTo(debtor.netAmount()) < 0)) {
                        debtor = balance;
                    }
                    if (balance.netAmount().isPositive() && (creditor == null
                            || balance.netAmount().compareTo(creditor.netAmount()) > 0)) {
                        creditor = balance;
                    }
                }

                MemberBalance suggestedDebtor = debtor;
                MemberBalance suggestedCreditor = creditor;
                Money suggestedAmount = Money.zero();
                if (suggestedDebtor != null && suggestedCreditor != null) {
                    Money owed = suggestedDebtor.netAmount().negate();
                    suggestedAmount = owed.compareTo(suggestedCreditor.netAmount()) <= 0
                            ? owed
                            : suggestedCreditor.netAmount();
                }

                Money finalSuggestedAmount = suggestedAmount;
                runOnUiThread(() -> {
                    members.clear();
                    members.addAll(options);
                    spinnerPaidBy.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, members));
                    spinnerPaidTo.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, members));

                    int paidByIndex = 0;
                    int paidToIndex = members.size() > 1 ? 1 : 0;
                    if (suggestedDebtor != null && suggestedCreditor != null && !finalSuggestedAmount.isZero()) {
                        paidByIndex = indexOf(suggestedDebtor.userId());
                        paidToIndex = indexOf(suggestedCreditor.userId());
                        inputAmount.setText(finalSuggestedAmount.toString());
                        String from = nameOf(suggestedDebtor.userId());
                        String to = nameOf(suggestedCreditor.userId());
                        textSettlementHint.setVisibility(View.VISIBLE);
                        textSettlementHint.setText(getString(
                                R.string.settlement_hint,
                                from,
                                UiHelpers.formatMoney(finalSuggestedAmount),
                                to));
                    }
                    spinnerPaidBy.setSelection(Math.max(paidByIndex, 0));
                    spinnerPaidTo.setSelection(Math.max(paidToIndex, 0));
                });
            } catch (Exception error) {
                runOnUiThread(() -> UiHelpers.showError(this, error));
            }
        });

        buttonSaveSettlement.setOnClickListener(v -> {
            MemberOption paidBy = (MemberOption) spinnerPaidBy.getSelectedItem();
            MemberOption paidTo = (MemberOption) spinnerPaidTo.getSelectedItem();
            String amountText = textOf(inputAmount);
            if (paidBy == null || paidTo == null) {
                UiHelpers.showError(this, getString(R.string.error_select_members));
                return;
            }
            if (paidBy.userId().equals(paidTo.userId())) {
                UiHelpers.showError(this, getString(R.string.error_same_settlement_parties));
                return;
            }
            if (amountText.isEmpty()) {
                inputAmount.setError(getString(R.string.error_required));
                inputAmount.requestFocus();
                return;
            }
            inputAmount.setError(null);

            UiHelpers.setSaving(buttonSaveSettlement, true, idleLabel);
            AppExecutors.io(() -> {
                try {
                    UiHelpers.services(this).settlements().createSettlement(
                            groupId,
                            paidBy.userId(),
                            paidTo.userId(),
                            Money.of(amountText),
                            LocalDate.now());
                    runOnUiThread(() -> {
                        UiHelpers.showSuccess(this, R.string.saved_settlement);
                        finish();
                    });
                } catch (Exception error) {
                    runOnUiThread(() -> {
                        UiHelpers.setSaving(buttonSaveSettlement, false, idleLabel);
                        UiHelpers.showError(this, error);
                    });
                }
            });
        });
    }

    private int indexOf(UserId userId) {
        for (int index = 0; index < members.size(); index++) {
            if (members.get(index).userId().equals(userId)) {
                return index;
            }
        }
        return 0;
    }

    private String nameOf(UserId userId) {
        for (MemberOption member : members) {
            if (member.userId().equals(userId)) {
                return member.toString();
            }
        }
        return userId.value();
    }

    private static String textOf(EditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString().trim();
    }
}
