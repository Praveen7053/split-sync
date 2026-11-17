package com.splitsync.domain.model;

import com.splitsync.domain.type.MembershipRole;
import com.splitsync.domain.validation.DomainValidator;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.MembershipId;
import com.splitsync.domain.valueobject.UserId;

import java.time.Instant;
import java.util.Objects;

/**
 * Links a user to a group with a role. Group creator starts as OWNER.
 */
public final class Membership {

    private final MembershipId membershipId;
    private final GroupId groupId;
    private final UserId userId;
    private final MembershipRole role;
    private final Instant createdAt;
    private final Instant updatedAt;

    private Membership(
            MembershipId membershipId,
            GroupId groupId,
            UserId userId,
            MembershipRole role,
            Instant createdAt,
            Instant updatedAt) {
        this.membershipId = membershipId;
        this.groupId = groupId;
        this.userId = userId;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Membership createOwner(GroupId groupId, UserId userId) {
        return create(groupId, userId, MembershipRole.OWNER);
    }

    public static Membership create(GroupId groupId, UserId userId, MembershipRole role) {
        Instant now = Instant.now();
        return new Membership(
                MembershipId.generate(),
                DomainValidator.requireNonNull(groupId, "groupId"),
                DomainValidator.requireNonNull(userId, "userId"),
                DomainValidator.requireNonNull(role, "role"),
                now,
                now);
    }

    public static Membership restore(
            MembershipId membershipId,
            GroupId groupId,
            UserId userId,
            MembershipRole role,
            Instant createdAt,
            Instant updatedAt) {
        return new Membership(
                DomainValidator.requireNonNull(membershipId, "membershipId"),
                DomainValidator.requireNonNull(groupId, "groupId"),
                DomainValidator.requireNonNull(userId, "userId"),
                DomainValidator.requireNonNull(role, "role"),
                DomainValidator.requireNonNull(createdAt, "createdAt"),
                DomainValidator.requireNonNull(updatedAt, "updatedAt"));
    }

    public MembershipId membershipId() {
        return membershipId;
    }

    public GroupId groupId() {
        return groupId;
    }

    public UserId userId() {
        return userId;
    }

    public MembershipRole role() {
        return role;
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
        if (!(other instanceof Membership membership)) {
            return false;
        }
        return membershipId.equals(membership.membershipId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(membershipId);
    }
}
