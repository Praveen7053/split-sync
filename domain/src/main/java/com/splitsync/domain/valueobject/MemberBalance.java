package com.splitsync.domain.valueobject;

import java.util.Objects;

/**
 * Net balance for one member in a group.
 * Positive means others owe this member; negative means this member owes others.
 */
public final class MemberBalance {

    private final UserId userId;
    private final Money netAmount;

    public MemberBalance(UserId userId, Money netAmount) {
        this.userId = Objects.requireNonNull(userId, "userId");
        this.netAmount = Objects.requireNonNull(netAmount, "netAmount");
    }

    public UserId userId() {
        return userId;
    }

    public Money netAmount() {
        return netAmount;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MemberBalance that)) {
            return false;
        }
        return userId.equals(that.userId) && netAmount.equals(that.netAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, netAmount);
    }
}
