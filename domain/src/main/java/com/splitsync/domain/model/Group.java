package com.splitsync.domain.model;

import com.splitsync.domain.type.GroupType;
import com.splitsync.domain.validation.DomainValidator;
import com.splitsync.domain.valueobject.CurrencyCode;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.UserId;

import java.time.Instant;
import java.util.Objects;

/**
 * Shared-expense group. Currency is fixed for the group lifetime in V1.
 */
public final class Group {

    private final GroupId groupId;
    private final String name;
    private final GroupType groupType;
    private final String description;
    private final CurrencyCode currency;
    private final UserId createdBy;
    private final Instant createdAt;
    private final Instant updatedAt;

    private Group(
            GroupId groupId,
            String name,
            GroupType groupType,
            String description,
            CurrencyCode currency,
            UserId createdBy,
            Instant createdAt,
            Instant updatedAt) {
        this.groupId = groupId;
        this.name = name;
        this.groupType = groupType;
        this.description = description;
        this.currency = currency;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Group create(String name, GroupType groupType, CurrencyCode currency, UserId createdBy) {
        Instant now = Instant.now();
        return new Group(
                GroupId.generate(),
                DomainValidator.requireText(name, "name"),
                DomainValidator.requireNonNull(groupType, "groupType"),
                null,
                DomainValidator.requireNonNull(currency, "currency"),
                DomainValidator.requireNonNull(createdBy, "createdBy"),
                now,
                now);
    }

    public static Group restore(
            GroupId groupId,
            String name,
            GroupType groupType,
            String description,
            CurrencyCode currency,
            UserId createdBy,
            Instant createdAt,
            Instant updatedAt) {
        return new Group(
                DomainValidator.requireNonNull(groupId, "groupId"),
                DomainValidator.requireText(name, "name"),
                DomainValidator.requireNonNull(groupType, "groupType"),
                blankToNull(description),
                DomainValidator.requireNonNull(currency, "currency"),
                DomainValidator.requireNonNull(createdBy, "createdBy"),
                DomainValidator.requireNonNull(createdAt, "createdAt"),
                DomainValidator.requireNonNull(updatedAt, "updatedAt"));
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    public GroupId groupId() {
        return groupId;
    }

    public String name() {
        return name;
    }

    public GroupType groupType() {
        return groupType;
    }

    public String description() {
        return description;
    }

    public CurrencyCode currency() {
        return currency;
    }

    public UserId createdBy() {
        return createdBy;
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
        if (!(other instanceof Group group)) {
            return false;
        }
        return groupId.equals(group.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId);
    }
}
