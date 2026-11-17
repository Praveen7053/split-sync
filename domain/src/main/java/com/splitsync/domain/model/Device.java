package com.splitsync.domain.model;

import com.splitsync.domain.validation.DomainValidator;
import com.splitsync.domain.valueobject.DeviceId;
import com.splitsync.domain.valueobject.UserId;

import java.time.Instant;
import java.util.Objects;

/**
 * One SplitSync installation. V1 keeps one active local user per device.
 */
public final class Device {

    private final DeviceId deviceId;
    private final UserId userId;
    private final String deviceName;
    private final Instant createdAt;
    private final Instant updatedAt;

    private Device(DeviceId deviceId, UserId userId, String deviceName, Instant createdAt, Instant updatedAt) {
        this.deviceId = deviceId;
        this.userId = userId;
        this.deviceName = deviceName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Device create(UserId userId, String deviceName) {
        Instant now = Instant.now();
        return new Device(
                DeviceId.generate(),
                DomainValidator.requireNonNull(userId, "userId"),
                DomainValidator.requireText(deviceName, "deviceName"),
                now,
                now);
    }

    public static Device restore(DeviceId deviceId, UserId userId, String deviceName, Instant createdAt, Instant updatedAt) {
        return new Device(
                DomainValidator.requireNonNull(deviceId, "deviceId"),
                DomainValidator.requireNonNull(userId, "userId"),
                DomainValidator.requireText(deviceName, "deviceName"),
                DomainValidator.requireNonNull(createdAt, "createdAt"),
                DomainValidator.requireNonNull(updatedAt, "updatedAt"));
    }

    public DeviceId deviceId() {
        return deviceId;
    }

    public UserId userId() {
        return userId;
    }

    public String deviceName() {
        return deviceName;
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
        if (!(other instanceof Device device)) {
            return false;
        }
        return deviceId.equals(device.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId);
    }
}
