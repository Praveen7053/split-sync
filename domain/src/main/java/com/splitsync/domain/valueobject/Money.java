package com.splitsync.domain.valueobject;

import com.splitsync.domain.exception.ValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Exact money amount with scale 2. Never uses floating-point types.
 */
public final class Money implements Comparable<Money> {

    public static final int SCALE = 2;

    private final BigDecimal amount;

    private Money(BigDecimal amount) {
        this.amount = amount;
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO.setScale(SCALE, RoundingMode.UNNECESSARY));
    }

    public static Money of(String amount) {
        if (amount == null || amount.isBlank()) {
            throw new ValidationException("amount is required");
        }
        try {
            return of(new BigDecimal(amount.trim()));
        } catch (NumberFormatException ex) {
            throw new ValidationException("amount must be a valid decimal number");
        }
    }

    public static Money of(BigDecimal amount) {
        if (amount == null) {
            throw new ValidationException("amount is required");
        }
        // Normalize to exactly 2 decimal places for all financial math.
        BigDecimal normalized = amount.setScale(SCALE, RoundingMode.HALF_UP);
        return new Money(normalized);
    }

    public static Money ofMinorUnits(long minorUnits) {
        BigDecimal value = BigDecimal.valueOf(minorUnits, SCALE);
        return new Money(value);
    }

    public BigDecimal amount() {
        return amount;
    }

    public long toMinorUnits() {
        return amount.movePointRight(SCALE).longValueExact();
    }

    public Money plus(Money other) {
        requireSame(other);
        return of(amount.add(other.amount));
    }

    public Money minus(Money other) {
        requireSame(other);
        return of(amount.subtract(other.amount));
    }

    public Money negate() {
        return of(amount.negate());
    }

    public boolean isPositive() {
        return amount.signum() > 0;
    }

    public boolean isZero() {
        return amount.signum() == 0;
    }

    public boolean isNegative() {
        return amount.signum() < 0;
    }

    public void requirePositive(String fieldName) {
        if (!isPositive()) {
            throw new ValidationException(fieldName + " must be greater than zero");
        }
    }

    private void requireSame(Money other) {
        if (other == null) {
            throw new ValidationException("money operand is required");
        }
    }

    @Override
    public int compareTo(Money other) {
        return amount.compareTo(other.amount);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Money money)) {
            return false;
        }
        return amount.compareTo(money.amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount.stripTrailingZeros());
    }

    @Override
    public String toString() {
        return amount.toPlainString();
    }
}
