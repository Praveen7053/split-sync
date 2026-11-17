package com.splitsync.domain.valueobject;

import com.splitsync.domain.exception.ValidationException;

import java.util.Objects;

/**
 * ISO-like currency code for a group (one currency per group in V1).
 */
public final class CurrencyCode {

    private final String value;

    private CurrencyCode(String value) {
        this.value = value;
    }

    public static CurrencyCode of(String value) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("currency is required");
        }
        String normalized = value.trim().toUpperCase();
        if (normalized.length() != 3) {
            throw new ValidationException("currency must be a 3-letter code");
        }
        return new CurrencyCode(normalized);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CurrencyCode currencyCode)) {
            return false;
        }
        return value.equals(currencyCode.value);
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
