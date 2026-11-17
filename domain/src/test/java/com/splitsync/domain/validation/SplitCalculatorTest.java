package com.splitsync.domain.validation;

import com.splitsync.domain.exception.ValidationException;
import com.splitsync.domain.model.ExpenseSplit;
import com.splitsync.domain.type.SplitType;
import com.splitsync.domain.valueobject.ExpenseId;
import com.splitsync.domain.valueobject.Money;
import com.splitsync.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SplitCalculatorTest {

    // Lexicographically ordered: a... < b... < c...
    private static final UserId USER_A = UserId.of("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    private static final UserId USER_B = UserId.of("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
    private static final UserId USER_C = UserId.of("cccccccc-cccc-cccc-cccc-cccccccccccc");
    private static final ExpenseId EXPENSE_ID = ExpenseId.of("11111111-1111-1111-1111-111111111111");

    @Test
    void equalSplitAssignsRemainderByStableUserIdOrder() {
        List<SplitParticipantInput> participants = List.of(
                SplitParticipantInput.equalShare(USER_C),
                SplitParticipantInput.equalShare(USER_A),
                SplitParticipantInput.equalShare(USER_B));

        List<ExpenseSplit> splits = SplitCalculator.calculate(EXPENSE_ID, Money.of("100.00"), SplitType.EQUAL, participants);

        Map<UserId, Money> byUser = toMap(splits);
        assertEquals(Money.of("33.34"), byUser.get(USER_A));
        assertEquals(Money.of("33.33"), byUser.get(USER_B));
        assertEquals(Money.of("33.33"), byUser.get(USER_C));
        assertEquals(Money.of("100.00"), sum(splits));
    }

    @Test
    void exactSplitRequiresExactTotal() {
        List<SplitParticipantInput> participants = List.of(
                SplitParticipantInput.exact(USER_A, new BigDecimal("500.00")),
                SplitParticipantInput.exact(USER_B, new BigDecimal("300.00")),
                SplitParticipantInput.exact(USER_C, new BigDecimal("200.00")));

        List<ExpenseSplit> splits = SplitCalculator.calculate(EXPENSE_ID, Money.of("1000.00"), SplitType.EXACT, participants);
        assertEquals(Money.of("1000.00"), sum(splits));
    }

    @Test
    void exactSplitRejectsMismatchedTotal() {
        List<SplitParticipantInput> participants = List.of(
                SplitParticipantInput.exact(USER_A, new BigDecimal("40.00")),
                SplitParticipantInput.exact(USER_B, new BigDecimal("40.00")));

        assertThrows(ValidationException.class, () -> SplitCalculator.calculate(EXPENSE_ID, Money.of("100.00"), SplitType.EXACT, participants));
    }

    @Test
    void percentageSplitReconcilesRounding() {
        List<SplitParticipantInput> participants = List.of(
                SplitParticipantInput.percentage(USER_A, new BigDecimal("33.33")),
                SplitParticipantInput.percentage(USER_B, new BigDecimal("33.33")),
                SplitParticipantInput.percentage(USER_C, new BigDecimal("33.34")));

        List<ExpenseSplit> splits = SplitCalculator.calculate(EXPENSE_ID, Money.of("100.00"), SplitType.PERCENTAGE, participants);
        assertEquals(Money.of("100.00"), sum(splits));
    }

    @Test
    void percentageSplitRejectsNonHundredTotal() {
        List<SplitParticipantInput> participants = List.of(
                SplitParticipantInput.percentage(USER_A, new BigDecimal("50")),
                SplitParticipantInput.percentage(USER_B, new BigDecimal("40")));

        assertThrows(ValidationException.class, () -> SplitCalculator.calculate(EXPENSE_ID, Money.of("100.00"), SplitType.PERCENTAGE, participants));
    }

    @Test
    void sharesSplitAllocatesProportionallyWithRemainderByUserId() {
        List<SplitParticipantInput> participants = List.of(
                SplitParticipantInput.shares(USER_A, new BigDecimal("2")),
                SplitParticipantInput.shares(USER_B, new BigDecimal("1")),
                SplitParticipantInput.shares(USER_C, new BigDecimal("1")));

        List<ExpenseSplit> splits = SplitCalculator.calculate(EXPENSE_ID, Money.of("100.00"), SplitType.SHARES, participants);

        Map<UserId, Money> byUser = toMap(splits);
        assertEquals(Money.of("50.00"), byUser.get(USER_A));
        assertEquals(Money.of("25.00"), byUser.get(USER_B));
        assertEquals(Money.of("25.00"), byUser.get(USER_C));
        assertEquals(Money.of("100.00"), sum(splits));
    }

    @Test
    void rejectsDuplicateParticipants() {
        List<SplitParticipantInput> participants = List.of(
                SplitParticipantInput.equalShare(USER_A),
                SplitParticipantInput.equalShare(USER_A));

        assertThrows(ValidationException.class, () -> SplitCalculator.calculate(EXPENSE_ID, Money.of("10.00"), SplitType.EQUAL, participants));
    }

    private static Map<UserId, Money> toMap(List<ExpenseSplit> splits) {
        return splits.stream().collect(Collectors.toMap(ExpenseSplit::userId, ExpenseSplit::allocatedAmount));
    }

    private static Money sum(List<ExpenseSplit> splits) {
        Money total = Money.zero();
        for (ExpenseSplit split : splits) {
            total = total.plus(split.allocatedAmount());
        }
        return total;
    }
}
