package com.restaurantmanager.api.domain.exception;

/**
 * Exception thrown when an operation results in a conflict (e.g., referential integrity violation).
 */
public class ConflictException extends DomainException {

    private final String resource;
    private final String reason;

    public ConflictException(String resource, String reason) {
        super(String.format("Conflict on %s: %s", resource, reason));
        this.resource = resource;
        this.reason = reason;
    }

    public ConflictException(String message) {
        super(message);
        this.resource = null;
        this.reason = null;
    }

    public String getResource() {
        return resource;
    }

    public String getReason() {
        return reason;
    }
}

