package com.splitsync.domain.model;

import com.splitsync.domain.validation.DomainValidator;
import com.splitsync.domain.valueobject.CurrencyCode;
import com.splitsync.domain.valueobject.ExpenseId;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.Money;
import com.splitsync.domain.valueobject.UserId;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Shared financial record inside one group.
 */
public final class Expense {

    private final ExpenseId expenseId;
    private final GroupId groupId;
    private final String description;
    private final Money amount;
    private final CurrencyCode currency;
    private final UserId paidBy;
    private final UserId createdBy;
    private final LocalDate expenseDate;
    private final String category;
    private final String notes;
    private final Instant createdAt;
    private final Instant updatedAt;

    private Expense(
            ExpenseId expenseId,
            GroupId groupId,
            String description,
            Money amount,
            CurrencyCode currency,
            UserId paidBy,
            UserId createdBy,
            LocalDate expenseDate,
            String category,
            String notes,
            Instant createdAt,
            Instant updatedAt) {
        this.expenseId = expenseId;
        this.groupId = groupId;
        this.description = description;
        this.amount = amount;
        this.currency = currency;
        this.paidBy = paidBy;
        this.createdBy = createdBy;
        this.expenseDate = expenseDate;
        this.category = category;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Expense create(
            GroupId groupId,
            String description,
            Money amount,
            CurrencyCode currency,
            UserId paidBy,
            UserId createdBy,
            LocalDate expenseDate) {
        DomainValidator.requireNonNull(amount, "amount").requirePositive("amount");
        Instant now = Instant.now();
        return new Expense(
                ExpenseId.generate(),
                DomainValidator.requireNonNull(groupId, "groupId"),
                DomainValidator.requireText(description, "description"),
                amount,
                DomainValidator.requireNonNull(currency, "currency"),
                DomainValidator.requireNonNull(paidBy, "paidBy"),
                DomainValidator.requireNonNull(createdBy, "createdBy"),
                DomainValidator.requireNonNull(expenseDate, "expenseDate"),
                null,
                null,
                now,
                now);
    }

    public static Expense restore(
            ExpenseId expenseId,
            GroupId groupId,
            String description,
            Money amount,
            CurrencyCode currency,
            UserId paidBy,
            UserId createdBy,
            LocalDate expenseDate,
            String category,
            String notes,
            Instant createdAt,
            Instant updatedAt) {
        DomainValidator.requireNonNull(amount, "amount").requirePositive("amount");
        return new Expense(
                DomainValidator.requireNonNull(expenseId, "expenseId"),
                DomainValidator.requireNonNull(groupId, "groupId"),
                DomainValidator.requireText(description, "description"),
                amount,
                DomainValidator.requireNonNull(currency, "currency"),
                DomainValidator.requireNonNull(paidBy, "paidBy"),
                DomainValidator.requireNonNull(createdBy, "createdBy"),
                DomainValidator.requireNonNull(expenseDate, "expenseDate"),
                blankToNull(category),
                blankToNull(notes),
                DomainValidator.requireNonNull(createdAt, "createdAt"),
                DomainValidator.requireNonNull(updatedAt, "updatedAt"));
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    public ExpenseId expenseId() {
        return expenseId;
    }

    public GroupId groupId() {
        return groupId;
    }

    public String description() {
        return description;
    }

    public Money amount() {
        return amount;
    }

    public CurrencyCode currency() {
        return currency;
    }

    public UserId paidBy() {
        return paidBy;
    }

    public UserId createdBy() {
        return createdBy;
    }

    public LocalDate expenseDate() {
        return expenseDate;
    }

    public String category() {
        return category;
    }

    public String notes() {
        return notes;
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
        if (!(other instanceof Expense expense)) {
            return false;
        }
        return expenseId.equals(expense.expenseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expenseId);
    }
}
