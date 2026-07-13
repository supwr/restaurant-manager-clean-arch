package com.restaurantmanager.api.domain.exception;

public class EntityNotFoundException extends DomainException {

    private final String entityType;
    private final String entityId;

    public EntityNotFoundException(String entityType, String entityId) {
        super(String.format("%s with id '%s' not found", entityType, entityId));
        this.entityType = entityType;
        this.entityId = entityId;
    }

    public EntityNotFoundException(String message) {
        super(message);
        this.entityType = null;
        this.entityId = null;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getEntityId() {
        return entityId;
    }
}

