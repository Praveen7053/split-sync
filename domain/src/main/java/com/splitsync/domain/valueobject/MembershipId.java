package com.splitsync.domain.valueobject;

import com.splitsync.domain.exception.ValidationException;

import java.util.Objects;
import java.util.UUID;

/**
 * Stable locally generated membership identity.
 */
public final class MembershipId {

    private final String value;

    private MembershipId(String value) {
        this.value = value;
    }

    public static MembershipId generate() {
        return new MembershipId(UUID.randomUUID().toString());
    }

    public static MembershipId of(String value) {
        requireUuid(value, "membershipId");
        return new MembershipId(value);
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
        if (!(other instanceof MembershipId membershipId)) {
            return false;
        }
        return value.equals(membershipId.value);
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
