package com.restaurantmanager.api.domain.exception;

/**
 * Base exception for domain layer.
 * All domain exceptions should extend this class.
 */
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}

