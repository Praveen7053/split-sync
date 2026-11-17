package com.splitsync.domain.valueobject;

import com.splitsync.domain.exception.ValidationException;

import java.util.Objects;
import java.util.UUID;

/**
 * Stable locally generated user identity.
 */
public final class UserId implements Comparable<UserId> {

    private final String value;

    private UserId(String value) {
        this.value = value;
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID().toString());
    }

    public static UserId of(String value) {
        requireUuid(value, "userId");
        return new UserId(value);
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
    public int compareTo(UserId other) {
        return value.compareTo(other.value);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UserId userId)) {
            return false;
        }
        return value.equals(userId.value);
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
