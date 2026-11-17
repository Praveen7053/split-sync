package com.splitsync.domain.validation;

import com.splitsync.domain.exception.ValidationException;
import com.splitsync.domain.model.ExpenseSplit;
import com.splitsync.domain.type.SplitType;
import com.splitsync.domain.valueobject.ExpenseId;
import com.splitsync.domain.valueobject.Money;
import com.splitsync.domain.valueobject.UserId;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Builds ExpenseSplit rows so allocated amounts always sum exactly to the expense total.
 */
public final class SplitCalculator {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private SplitCalculator() {
    }

    public static List<ExpenseSplit> calculate(
            ExpenseId expenseId,
            Money expenseAmount,
            SplitType splitType,
            List<SplitParticipantInput> participants) {
        DomainValidator.requireNonNull(expenseId, "expenseId");
        DomainValidator.requireNonNull(expenseAmount, "expenseAmount").requirePositive("expenseAmount");
        DomainValidator.requireNonNull(splitType, "splitType");
        requireParticipants(participants);

        return switch (splitType) {
            case EQUAL -> calculateEqual(expenseId, expenseAmount, participants);
            case EXACT -> calculateExact(expenseId, expenseAmount, participants);
            case PERCENTAGE -> calculatePercentage(expenseId, expenseAmount, participants);
            case SHARES -> calculateShares(expenseId, expenseAmount, participants);
        };
    }

    private static List<ExpenseSplit> calculateEqual(
            ExpenseId expenseId,
            Money expenseAmount,
            List<SplitParticipantInput> participants) {
        List<UserId> orderedUserIds = sortedUniqueUserIds(participants);
        long totalCents = expenseAmount.toMinorUnits();
        int memberCount = orderedUserIds.size();
        long baseCents = totalCents / memberCount;
        long remainderCents = totalCents % memberCount;

        List<ExpenseSplit> splits = new ArrayList<>();
        for (int index = 0; index < orderedUserIds.size(); index++) {
            // Remainder cents go first in stable userId order.
            long allocatedCents = baseCents + (index < remainderCents ? 1 : 0);
            splits.add(ExpenseSplit.create(
                    expenseId,
                    orderedUserIds.get(index),
                    SplitType.EQUAL,
                    BigDecimal.ONE,
                    Money.ofMinorUnits(allocatedCents)));
        }
        return splits;
    }

    private static List<ExpenseSplit> calculateExact(
            ExpenseId expenseId,
            Money expenseAmount,
            List<SplitParticipantInput> participants) {
        List<ExpenseSplit> splits = new ArrayList<>();
        Money allocatedTotal = Money.zero();

        for (SplitParticipantInput participant : orderedParticipants(participants)) {
            Money allocated = Money.of(requirePositiveShare(participant.shareValue(), "exact amount"));
            allocatedTotal = allocatedTotal.plus(allocated);
            splits.add(ExpenseSplit.create(
                    expenseId,
                    participant.userId(),
                    SplitType.EXACT,
                    allocated.amount(),
                    allocated));
        }

        if (!allocatedTotal.equals(expenseAmount)) {
            throw new ValidationException("exact split amounts must sum to expense amount");
        }
        return splits;
    }

    private static List<ExpenseSplit> calculatePercentage(
            ExpenseId expenseId,
            Money expenseAmount,
            List<SplitParticipantInput> participants) {
        List<SplitParticipantInput> ordered = orderedParticipants(participants);
        BigDecimal percentTotal = BigDecimal.ZERO;
        for (SplitParticipantInput participant : ordered) {
            BigDecimal percent = requirePositiveShare(participant.shareValue(), "percentage");
            percentTotal = percentTotal.add(percent);
        }
        if (percentTotal.compareTo(ONE_HUNDRED) != 0) {
            throw new ValidationException("percentages must sum to 100");
        }

        long totalCents = expenseAmount.toMinorUnits();
        long[] floorCents = new long[ordered.size()];
        long assignedCents = 0;
        for (int index = 0; index < ordered.size(); index++) {
            BigDecimal percent = ordered.get(index).shareValue();
            // Floor each share in cents, then distribute leftover by userId order.
            long cents = BigDecimal.valueOf(totalCents)
                    .multiply(percent)
                    .divide(ONE_HUNDRED, 0, RoundingMode.DOWN)
                    .longValueExact();
            floorCents[index] = cents;
            assignedCents += cents;
        }

        long remainderCents = totalCents - assignedCents;
        return buildRoundedSplits(expenseId, SplitType.PERCENTAGE, ordered, floorCents, remainderCents);
    }

    private static List<ExpenseSplit> calculateShares(
            ExpenseId expenseId,
            Money expenseAmount,
            List<SplitParticipantInput> participants) {
        List<SplitParticipantInput> ordered = orderedParticipants(participants);
        BigDecimal totalShares = BigDecimal.ZERO;
        for (SplitParticipantInput participant : ordered) {
            BigDecimal shares = requirePositiveShare(participant.shareValue(), "shares");
            totalShares = totalShares.add(shares);
        }

        long totalCents = expenseAmount.toMinorUnits();
        long[] floorCents = new long[ordered.size()];
        long assignedCents = 0;
        for (int index = 0; index < ordered.size(); index++) {
            BigDecimal shares = ordered.get(index).shareValue();
            long cents = BigDecimal.valueOf(totalCents)
                    .multiply(shares)
                    .divide(totalShares, 0, RoundingMode.DOWN)
                    .longValueExact();
            floorCents[index] = cents;
            assignedCents += cents;
        }

        long remainderCents = totalCents - assignedCents;
        return buildRoundedSplits(expenseId, SplitType.SHARES, ordered, floorCents, remainderCents);
    }

    private static List<ExpenseSplit> buildRoundedSplits(
            ExpenseId expenseId,
            SplitType splitType,
            List<SplitParticipantInput> ordered,
            long[] floorCents,
            long remainderCents) {
        List<ExpenseSplit> splits = new ArrayList<>();
        for (int index = 0; index < ordered.size(); index++) {
            long allocatedCents = floorCents[index] + (index < remainderCents ? 1 : 0);
            SplitParticipantInput participant = ordered.get(index);
            splits.add(ExpenseSplit.create(
                    expenseId,
                    participant.userId(),
                    splitType,
                    participant.shareValue(),
                    Money.ofMinorUnits(allocatedCents)));
        }
        return splits;
    }

    private static void requireParticipants(List<SplitParticipantInput> participants) {
        if (participants == null || participants.isEmpty()) {
            throw new ValidationException("at least one split participant is required");
        }
        Set<UserId> seen = new HashSet<>();
        for (SplitParticipantInput participant : participants) {
            DomainValidator.requireNonNull(participant, "participant");
            if (!seen.add(participant.userId())) {
                throw new ValidationException("duplicate split participant: " + participant.userId());
            }
        }
    }

    private static List<UserId> sortedUniqueUserIds(List<SplitParticipantInput> participants) {
        List<UserId> userIds = new ArrayList<>();
        for (SplitParticipantInput participant : participants) {
            userIds.add(participant.userId());
        }
        userIds.sort(Comparator.naturalOrder());
        return userIds;
    }

    private static List<SplitParticipantInput> orderedParticipants(List<SplitParticipantInput> participants) {
        List<SplitParticipantInput> ordered = new ArrayList<>(participants);
        ordered.sort(Comparator.comparing(SplitParticipantInput::userId));
        return ordered;
    }

    private static BigDecimal requirePositiveShare(BigDecimal shareValue, String fieldName) {
        if (shareValue == null) {
            throw new ValidationException(fieldName + " is required");
        }
        if (shareValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(fieldName + " must be greater than zero");
        }
        return shareValue;
    }
}
