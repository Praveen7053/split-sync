package com.splitsync.application;

/**
 * Application-layer failure such as missing data or permission denial.
 */
public class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
    }
}
