package com.splitsync.domain.model;

import com.splitsync.domain.type.SplitType;
import com.splitsync.domain.validation.DomainValidator;
import com.splitsync.domain.valueobject.ExpenseId;
import com.splitsync.domain.valueobject.ExpenseSplitId;
import com.splitsync.domain.valueobject.Money;
import com.splitsync.domain.valueobject.UserId;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * One member's share of an expense. shareValue meaning depends on splitType.
 */
public final class ExpenseSplit {

    private final ExpenseSplitId expenseSplitId;
    private final ExpenseId expenseId;
    private final UserId userId;
    private final SplitType splitType;
    private final BigDecimal shareValue;
    private final Money allocatedAmount;
    private final Instant createdAt;
    private final Instant updatedAt;

    private ExpenseSplit(
            ExpenseSplitId expenseSplitId,
            ExpenseId expenseId,
            UserId userId,
            SplitType splitType,
            BigDecimal shareValue,
            Money allocatedAmount,
            Instant createdAt,
            Instant updatedAt) {
        this.expenseSplitId = expenseSplitId;
        this.expenseId = expenseId;
        this.userId = userId;
        this.splitType = splitType;
        this.shareValue = shareValue;
        this.allocatedAmount = allocatedAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ExpenseSplit create(
            ExpenseId expenseId,
            UserId userId,
            SplitType splitType,
            BigDecimal shareValue,
            Money allocatedAmount) {
        DomainValidator.requireNonNull(allocatedAmount, "allocatedAmount");
        if (allocatedAmount.isNegative()) {
            throw new com.splitsync.domain.exception.ValidationException("allocatedAmount cannot be negative");
        }
        Instant now = Instant.now();
        return new ExpenseSplit(
                ExpenseSplitId.generate(),
                DomainValidator.requireNonNull(expenseId, "expenseId"),
                DomainValidator.requireNonNull(userId, "userId"),
                DomainValidator.requireNonNull(splitType, "splitType"),
                DomainValidator.requireNonNull(shareValue, "shareValue"),
                allocatedAmount,
                now,
                now);
    }

    public static ExpenseSplit restore(
            ExpenseSplitId expenseSplitId,
            ExpenseId expenseId,
            UserId userId,
            SplitType splitType,
            BigDecimal shareValue,
            Money allocatedAmount,
            Instant createdAt,
            Instant updatedAt) {
        return new ExpenseSplit(
                DomainValidator.requireNonNull(expenseSplitId, "expenseSplitId"),
                DomainValidator.requireNonNull(expenseId, "expenseId"),
                DomainValidator.requireNonNull(userId, "userId"),
                DomainValidator.requireNonNull(splitType, "splitType"),
                DomainValidator.requireNonNull(shareValue, "shareValue"),
                DomainValidator.requireNonNull(allocatedAmount, "allocatedAmount"),
                DomainValidator.requireNonNull(createdAt, "createdAt"),
                DomainValidator.requireNonNull(updatedAt, "updatedAt"));
    }

    public ExpenseSplitId expenseSplitId() {
        return expenseSplitId;
    }

    public ExpenseId expenseId() {
        return expenseId;
    }

    public UserId userId() {
        return userId;
    }

    public SplitType splitType() {
        return splitType;
    }

    public BigDecimal shareValue() {
        return shareValue;
    }

    public Money allocatedAmount() {
        return allocatedAmount;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ExpenseSplit expenseSplit)) {
            return false;
        }
        return expenseSplitId.equals(expenseSplit.expenseSplitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expenseSplitId);
    }
}
