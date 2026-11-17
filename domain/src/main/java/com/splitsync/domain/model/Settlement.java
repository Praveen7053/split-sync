package com.splitsync.domain.model;

import com.splitsync.domain.exception.ValidationException;
import com.splitsync.domain.validation.DomainValidator;
import com.splitsync.domain.valueobject.CurrencyCode;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.Money;
import com.splitsync.domain.valueobject.SettlementId;
import com.splitsync.domain.valueobject.UserId;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Manual confirmation that paidBy paid paidTo. Not a real bank transfer.
 */
public final class Settlement {

    private final SettlementId settlementId;
    private final GroupId groupId;
    private final UserId paidBy;
    private final UserId paidTo;
    private final Money amount;
    private final CurrencyCode currency;
    private final UserId createdBy;
    private final LocalDate settlementDate;
    private final String notes;
    private final Instant createdAt;
    private final Instant updatedAt;

    private Settlement(
            SettlementId settlementId,
            GroupId groupId,
            UserId paidBy,
            UserId paidTo,
            Money amount,
            CurrencyCode currency,
            UserId createdBy,
            LocalDate settlementDate,
            String notes,
            Instant createdAt,
            Instant updatedAt) {
        this.settlementId = settlementId;
        this.groupId = groupId;
        this.paidBy = paidBy;
        this.paidTo = paidTo;
        this.amount = amount;
        this.currency = currency;
        this.createdBy = createdBy;
        this.settlementDate = settlementDate;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Settlement create(
            GroupId groupId,
            UserId paidBy,
            UserId paidTo,
            Money amount,
            CurrencyCode currency,
            UserId createdBy,
            LocalDate settlementDate) {
        DomainValidator.requireNonNull(amount, "amount").requirePositive("amount");
        DomainValidator.requireNonNull(paidBy, "paidBy");
        DomainValidator.requireNonNull(paidTo, "paidTo");
        if (paidBy.equals(paidTo)) {
            throw new ValidationException("paidBy and paidTo must be different users");
        }
        Instant now = Instant.now();
        return new Settlement(
                SettlementId.generate(),
                DomainValidator.requireNonNull(groupId, "groupId"),
                paidBy,
                paidTo,
                amount,
                DomainValidator.requireNonNull(currency, "currency"),
                DomainValidator.requireNonNull(createdBy, "createdBy"),
                DomainValidator.requireNonNull(settlementDate, "settlementDate"),
                null,
                now,
                now);
    }

    public static Settlement restore(
            SettlementId settlementId,
            GroupId groupId,
            UserId paidBy,
            UserId paidTo,
            Money amount,
            CurrencyCode currency,
            UserId createdBy,
            LocalDate settlementDate,
            String notes,
            Instant createdAt,
            Instant updatedAt) {
        DomainValidator.requireNonNull(amount, "amount").requirePositive("amount");
        DomainValidator.requireNonNull(paidBy, "paidBy");
        DomainValidator.requireNonNull(paidTo, "paidTo");
        if (paidBy.equals(paidTo)) {
            throw new ValidationException("paidBy and paidTo must be different users");
        }
        return new Settlement(
                DomainValidator.requireNonNull(settlementId, "settlementId"),
                DomainValidator.requireNonNull(groupId, "groupId"),
                paidBy,
                paidTo,
                amount,
                DomainValidator.requireNonNull(currency, "currency"),
                DomainValidator.requireNonNull(createdBy, "createdBy"),
                DomainValidator.requireNonNull(settlementDate, "settlementDate"),
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

    public SettlementId settlementId() {
        return settlementId;
    }

    public GroupId groupId() {
        return groupId;
    }

    public UserId paidBy() {
        return paidBy;
    }

    public UserId paidTo() {
        return paidTo;
    }

    public Money amount() {
        return amount;
    }

    public CurrencyCode currency() {
        return currency;
    }

    public UserId createdBy() {
        return createdBy;
    }

    public LocalDate settlementDate() {
        return settlementDate;
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
        if (!(other instanceof Settlement settlement)) {
            return false;
        }
        return settlementId.equals(settlement.settlementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(settlementId);
    }
}
