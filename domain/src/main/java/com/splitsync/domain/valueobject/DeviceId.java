package com.splitsync.domain.valueobject;

import com.splitsync.domain.exception.ValidationException;

import java.util.Objects;
import java.util.UUID;

/**
 * Stable locally generated device / installation identity.
 */
public final class DeviceId {

    private final String value;

    private DeviceId(String value) {
        this.value = value;
    }

    public static DeviceId generate() {
        return new DeviceId(UUID.randomUUID().toString());
    }

    public static DeviceId of(String value) {
        requireUuid(value, "deviceId");
        return new DeviceId(value);
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
        if (!(other instanceof DeviceId deviceId)) {
            return false;
        }
        return value.equals(deviceId.value);
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
