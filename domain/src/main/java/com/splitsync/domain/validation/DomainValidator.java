package com.splitsync.domain.validation;

import com.splitsync.domain.exception.ValidationException;

/**
 * Small shared validation helpers for domain construction.
 */
public final class DomainValidator {

    private DomainValidator() {
    }

    public static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(fieldName + " is required");
        }
        return value.trim();
    }

    public static <T> T requireNonNull(T value, String fieldName) {
        if (value == null) {
            throw new ValidationException(fieldName + " is required");
        }
        return value;
    }
}
