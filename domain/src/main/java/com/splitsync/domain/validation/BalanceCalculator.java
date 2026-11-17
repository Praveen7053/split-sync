package com.splitsync.domain.validation;

import com.splitsync.domain.model.Expense;
import com.splitsync.domain.model.ExpenseSplit;
import com.splitsync.domain.model.Settlement;
import com.splitsync.domain.valueobject.MemberBalance;
import com.splitsync.domain.valueobject.Money;
import com.splitsync.domain.valueobject.UserId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Derives per-member net balances from expenses, splits, and settlements.
 * Positive net => others owe this member. Negative net => this member owes others.
 */
public final class BalanceCalculator {

    private BalanceCalculator() {
    }

    public static List<MemberBalance> calculate(
            List<Expense> expenses,
            List<ExpenseSplit> splits,
            List<Settlement> settlements) {
        List<Expense> safeExpenses = expenses == null ? List.of() : expenses;
        List<ExpenseSplit> safeSplits = splits == null ? List.of() : splits;
        List<Settlement> safeSettlements = settlements == null ? List.of() : settlements;

        Map<UserId, Money> paidByMember = new HashMap<>();
        Map<UserId, Money> owedByMember = new HashMap<>();
        Set<UserId> members = new HashSet<>();

        for (Expense expense : safeExpenses) {
            DomainValidator.requireNonNull(expense, "expense");
            members.add(expense.paidBy());
            paidByMember.merge(expense.paidBy(), expense.amount(), Money::plus);
        }

        for (ExpenseSplit split : safeSplits) {
            DomainValidator.requireNonNull(split, "split");
            members.add(split.userId());
            owedByMember.merge(split.userId(), split.allocatedAmount(), Money::plus);
        }

        // Start from paid - owed, then apply settlements.
        Map<UserId, Money> netByMember = new HashMap<>();
        for (UserId member : members) {
            Money paid = paidByMember.getOrDefault(member, Money.zero());
            Money owed = owedByMember.getOrDefault(member, Money.zero());
            netByMember.put(member, paid.minus(owed));
        }

        for (Settlement settlement : safeSettlements) {
            DomainValidator.requireNonNull(settlement, "settlement");
            members.add(settlement.paidBy());
            members.add(settlement.paidTo());

            // paidBy reduces their debt (balance goes up); paidTo is owed less (balance goes down).
            Money paidByNet = netByMember.getOrDefault(settlement.paidBy(), Money.zero());
            Money paidToNet = netByMember.getOrDefault(settlement.paidTo(), Money.zero());
            netByMember.put(settlement.paidBy(), paidByNet.plus(settlement.amount()));
            netByMember.put(settlement.paidTo(), paidToNet.minus(settlement.amount()));
        }

        List<MemberBalance> balances = new ArrayList<>();
        for (UserId member : members) {
            balances.add(new MemberBalance(member, netByMember.getOrDefault(member, Money.zero())));
        }
        balances.sort((left, right) -> left.userId().compareTo(right.userId()));
        return balances;
    }
}
