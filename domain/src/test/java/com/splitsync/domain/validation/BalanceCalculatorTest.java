package com.splitsync.domain.validation;

import com.splitsync.domain.model.Expense;
import com.splitsync.domain.model.ExpenseSplit;
import com.splitsync.domain.model.Settlement;
import com.splitsync.domain.type.SplitType;
import com.splitsync.domain.valueobject.CurrencyCode;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.MemberBalance;
import com.splitsync.domain.valueobject.Money;
import com.splitsync.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BalanceCalculatorTest {

    private static final UserId USER_A = UserId.of("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    private static final UserId USER_B = UserId.of("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
    private static final UserId USER_C = UserId.of("cccccccc-cccc-cccc-cccc-cccccccccccc");
    private static final GroupId GROUP_ID = GroupId.of("22222222-2222-2222-2222-222222222222");
    private static final CurrencyCode INR = CurrencyCode.of("INR");

    @Test
    void calculatesBalancesFromExpenseAndEqualSplits() {
        Expense expense = Expense.create(GROUP_ID, "Dinner", Money.of("900.00"), INR, USER_A, USER_A, LocalDate.of(2026, 1, 1));
        List<ExpenseSplit> splits = SplitCalculator.calculate(
                expense.expenseId(),
                expense.amount(),
                SplitType.EQUAL,
                List.of(
                        SplitParticipantInput.equalShare(USER_A),
                        SplitParticipantInput.equalShare(USER_B),
                        SplitParticipantInput.equalShare(USER_C)));

        Map<UserId, Money> balances = toMap(BalanceCalculator.calculate(List.of(expense), splits, List.of()));

        // A paid 900 and owes 300 => +600; B and C each owe 300.
        assertEquals(Money.of("600.00"), balances.get(USER_A));
        assertEquals(Money.of("-300.00"), balances.get(USER_B));
        assertEquals(Money.of("-300.00"), balances.get(USER_C));
    }

    @Test
    void settlementMovesBalanceFromReceiverToPayer() {
        Expense expense = Expense.create(GROUP_ID, "Taxi", Money.of("100.00"), INR, USER_A, USER_A, LocalDate.of(2026, 1, 2));
        List<ExpenseSplit> splits = SplitCalculator.calculate(
                expense.expenseId(),
                expense.amount(),
                SplitType.EQUAL,
                List.of(
                        SplitParticipantInput.equalShare(USER_A),
                        SplitParticipantInput.equalShare(USER_B)));

        // Before settlement: A +50, B -50. B pays A 50 => both zero.
        Settlement settlement = Settlement.create(
                GROUP_ID,
                USER_B,
                USER_A,
                Money.of("50.00"),
                INR,
                USER_B,
                LocalDate.of(2026, 1, 3));

        Map<UserId, Money> balances = toMap(BalanceCalculator.calculate(List.of(expense), splits, List.of(settlement)));

        assertEquals(Money.zero(), balances.get(USER_A));
        assertEquals(Money.zero(), balances.get(USER_B));
    }

    private static Map<UserId, Money> toMap(List<MemberBalance> balances) {
        return balances.stream().collect(Collectors.toMap(MemberBalance::userId, MemberBalance::netAmount));
    }
}
