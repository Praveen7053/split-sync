package com.splitsync.domain.validation;

import com.splitsync.domain.valueobject.UserId;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Participant input used by SplitCalculator before ExpenseSplit records are created.
 * shareValue meaning: EXACT=amount, PERCENTAGE=percent, SHARES=share count, EQUAL=ignored.
 */
public final class SplitParticipantInput {

    private final UserId userId;
    private final BigDecimal shareValue;

    public SplitParticipantInput(UserId userId, BigDecimal shareValue) {
        this.userId = DomainValidator.requireNonNull(userId, "userId");
        this.shareValue = shareValue;
    }

    public static SplitParticipantInput equalShare(UserId userId) {
        return new SplitParticipantInput(userId, BigDecimal.ONE);
    }

    public static SplitParticipantInput exact(UserId userId, BigDecimal amount) {
        return new SplitParticipantInput(userId, DomainValidator.requireNonNull(amount, "shareValue"));
    }

    public static SplitParticipantInput percentage(UserId userId, BigDecimal percent) {
        return new SplitParticipantInput(userId, DomainValidator.requireNonNull(percent, "shareValue"));
    }

    public static SplitParticipantInput shares(UserId userId, BigDecimal shares) {
        return new SplitParticipantInput(userId, DomainValidator.requireNonNull(shares, "shareValue"));
    }

    public UserId userId() {
        return userId;
    }

    public BigDecimal shareValue() {
        return shareValue;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SplitParticipantInput that)) {
            return false;
        }
        return userId.equals(that.userId) && Objects.equals(shareValue, that.shareValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, shareValue);
    }
}
