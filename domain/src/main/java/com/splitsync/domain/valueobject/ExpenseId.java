package com.splitsync.domain.valueobject;

import com.splitsync.domain.exception.ValidationException;

import java.util.Objects;
import java.util.UUID;

/**
 * Stable locally generated expense identity.
 */
public final class ExpenseId {

    private final String value;

    private ExpenseId(String value) {
        this.value = value;
    }

    public static ExpenseId generate() {
        return new ExpenseId(UUID.randomUUID().toString());
    }

    public static ExpenseId of(String value) {
        requireUuid(value, "expenseId");
        return new ExpenseId(value);
    }

    public String value() {
        return value;
    }

    private static void requireUuid(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(fieldName + " is required");
        }
        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            throw new ValidationException(fieldName + " must be a valid UUID");
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ExpenseId expenseId)) {
            return false;
        }
        return value.equals(expenseId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
