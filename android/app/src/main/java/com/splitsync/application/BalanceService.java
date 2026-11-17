package com.splitsync.application;

import com.splitsync.data.repository.ExpenseLocalRepository;
import com.splitsync.data.repository.GroupLocalRepository;
import com.splitsync.data.repository.SettlementLocalRepository;
import com.splitsync.domain.model.Expense;
import com.splitsync.domain.model.ExpenseSplit;
import com.splitsync.domain.model.Settlement;
import com.splitsync.domain.validation.BalanceCalculator;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.MemberBalance;

import java.util.List;

/**
 * Reads group expenses/settlements and derives member balances.
 */
public class BalanceService {

    private final ProfileService profiles;
    private final GroupLocalRepository groups;
    private final ExpenseLocalRepository expenses;
    private final SettlementLocalRepository settlements;
    private final MembershipGuard membershipGuard;

    public BalanceService(
            ProfileService profiles,
            GroupLocalRepository groups,
            ExpenseLocalRepository expenses,
            SettlementLocalRepository settlements) {
        this.profiles = profiles;
        this.groups = groups;
        this.expenses = expenses;
        this.settlements = settlements;
        this.membershipGuard = new MembershipGuard(groups);
    }

    public List<MemberBalance> getGroupBalances(GroupId groupId) {
        if (groups.findById(groupId) == null) {
            throw new ApplicationException("group not found");
        }
        membershipGuard.requireMembership(groupId, profiles.getLocalProfile().userId());

        List<Expense> groupExpenses = expenses.findByGroupId(groupId);
        List<ExpenseSplit> groupSplits = expenses.findSplitsForGroup(groupId);
        List<Settlement> groupSettlements = settlements.findByGroupId(groupId);
        return BalanceCalculator.calculate(groupExpenses, groupSplits, groupSettlements);
    }
}
