package com.splitsync.presentation;

import com.splitsync.domain.valueobject.UserId;

/**
 * Spinner/list display option for a group member.
 */
public final class MemberOption {

    private final UserId userId;
    private final String displayName;

    public MemberOption(UserId userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public UserId userId() {
        return userId;
    }

    public String displayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
