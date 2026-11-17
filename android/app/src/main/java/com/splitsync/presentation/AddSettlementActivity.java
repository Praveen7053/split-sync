package com.splitsync.presentation;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.splitsync.R;
import com.splitsync.domain.model.Membership;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.Money;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddSettlementActivity extends AppCompatActivity {

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
        MaterialButton buttonSaveSettlement = findViewById(R.id.buttonSaveSettlement);

        AppExecutors.io(() -> {
            try {
                List<Membership> memberships = UiHelpers.services(this).groups().listMembers(groupId);
                List<MemberOption> options = new ArrayList<>();
                for (Membership membership : memberships) {
                    options.add(new MemberOption(membership.userId(), UiHelpers.services(this).profiles().displayNameOf(membership.userId())));
                }
                runOnUiThread(() -> {
                    members.clear();
                    members.addAll(options);
                    ArrayAdapter<MemberOption> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, members);
                    spinnerPaidBy.setAdapter(adapter);
                    spinnerPaidTo.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, members));
                    if (members.size() > 1) {
                        spinnerPaidTo.setSelection(1);
                    }
                });
            } catch (Exception error) {
                runOnUiThread(() -> UiHelpers.showError(this, error));
            }
        });

        buttonSaveSettlement.setOnClickListener(v -> {
            MemberOption paidBy = (MemberOption) spinnerPaidBy.getSelectedItem();
            MemberOption paidTo = (MemberOption) spinnerPaidTo.getSelectedItem();
            String amountText = inputAmount.getText() == null ? "" : inputAmount.getText().toString().trim();
            AppExecutors.io(() -> {
                try {
                    UiHelpers.services(this).settlements().createSettlement(
                            groupId,
                            paidBy.userId(),
                            paidTo.userId(),
                            Money.of(amountText),
                            LocalDate.now());
                    runOnUiThread(this::finish);
                } catch (Exception error) {
                    runOnUiThread(() -> UiHelpers.showError(this, error));
                }
            });
        });
    }
}
