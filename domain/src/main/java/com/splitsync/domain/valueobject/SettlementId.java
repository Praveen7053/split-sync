package com.splitsync.domain.valueobject;

import com.splitsync.domain.exception.ValidationException;

import java.util.Objects;
import java.util.UUID;

/**
 * Stable locally generated settlement identity.
 */
public final class SettlementId {

    private final String value;

    private SettlementId(String value) {
        this.value = value;
    }

    public static SettlementId generate() {
        return new SettlementId(UUID.randomUUID().toString());
    }

    public static SettlementId of(String value) {
        requireUuid(value, "settlementId");
        return new SettlementId(value);
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
        if (!(other instanceof SettlementId settlementId)) {
            return false;
        }
        return value.equals(settlementId.value);
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
