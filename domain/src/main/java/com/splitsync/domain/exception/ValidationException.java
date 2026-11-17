package com.splitsync.domain.exception;

/**
 * Thrown when domain input fails validation before an operation proceeds.
 */
public class ValidationException extends DomainException {

    public ValidationException(String message) {
        super(message);
    }
}
