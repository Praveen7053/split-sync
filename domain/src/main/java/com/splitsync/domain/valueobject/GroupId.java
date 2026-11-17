package com.splitsync.domain.valueobject;

import com.splitsync.domain.exception.ValidationException;

import java.util.Objects;
import java.util.UUID;

/**
 * Stable locally generated group identity.
 */
public final class GroupId {

    private final String value;

    private GroupId(String value) {
        this.value = value;
    }

    public static GroupId generate() {
        return new GroupId(UUID.randomUUID().toString());
    }

    public static GroupId of(String value) {
        requireUuid(value, "groupId");
        return new GroupId(value);
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
        if (!(other instanceof GroupId groupId)) {
            return false;
        }
        return value.equals(groupId.value);
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
