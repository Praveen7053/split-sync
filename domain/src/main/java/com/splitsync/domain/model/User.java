package com.splitsync.domain.model;

import com.splitsync.domain.validation.DomainValidator;
import com.splitsync.domain.valueobject.UserId;

import java.time.Instant;
import java.util.Objects;

/**
 * Local user profile. Display name is required; contact fields are optional.
 */
public final class User {

    private final UserId userId;
    private final String displayName;
    private final String phoneNumber;
    private final String email;
    private final String profilePhotoPath;
    private final Instant createdAt;
    private final Instant updatedAt;

    private User(
            UserId userId,
            String displayName,
            String phoneNumber,
            String email,
            String profilePhotoPath,
            Instant createdAt,
            Instant updatedAt) {
        this.userId = userId;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.profilePhotoPath = profilePhotoPath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(String displayName) {
        Instant now = Instant.now();
        return new User(UserId.generate(), DomainValidator.requireText(displayName, "displayName"), null, null, null, now, now);
    }

    public static User restore(
            UserId userId,
            String displayName,
            String phoneNumber,
            String email,
            String profilePhotoPath,
            Instant createdAt,
            Instant updatedAt) {
        return new User(
                DomainValidator.requireNonNull(userId, "userId"),
                DomainValidator.requireText(displayName, "displayName"),
                blankToNull(phoneNumber),
                blankToNull(email),
                blankToNull(profilePhotoPath),
                DomainValidator.requireNonNull(createdAt, "createdAt"),
                DomainValidator.requireNonNull(updatedAt, "updatedAt"));
    }

    public User withProfile(String displayName, String phoneNumber, String email, String profilePhotoPath) {
        return new User(
                userId,
                DomainValidator.requireText(displayName, "displayName"),
                blankToNull(phoneNumber),
                blankToNull(email),
                blankToNull(profilePhotoPath),
                createdAt,
                Instant.now());
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    public UserId userId() {
        return userId;
    }

    public String displayName() {
        return displayName;
    }

    public String phoneNumber() {
        return phoneNumber;
    }

    public String email() {
        return email;
    }

    public String profilePhotoPath() {
        return profilePhotoPath;
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
        if (!(other instanceof User user)) {
            return false;
        }
        return userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
