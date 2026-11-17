package com.splitsync.domain.exception;

/**
 * Base exception for all domain rule violations.
 */
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }
}
