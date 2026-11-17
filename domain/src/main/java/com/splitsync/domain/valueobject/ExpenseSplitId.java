package com.splitsync.domain.valueobject;

import com.splitsync.domain.exception.ValidationException;

import java.util.Objects;
import java.util.UUID;

/**
 * Stable locally generated expense-split identity.
 */
public final class ExpenseSplitId {

    private final String value;

    private ExpenseSplitId(String value) {
        this.value = value;
    }

    public static ExpenseSplitId generate() {
        return new ExpenseSplitId(UUID.randomUUID().toString());
    }

    public static ExpenseSplitId of(String value) {
        requireUuid(value, "expenseSplitId");
        return new ExpenseSplitId(value);
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
        if (!(other instanceof ExpenseSplitId expenseSplitId)) {
            return false;
        }
        return value.equals(expenseSplitId.value);
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
