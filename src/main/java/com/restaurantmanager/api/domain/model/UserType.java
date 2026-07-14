package com.restaurantmanager.api.domain.model;

import com.restaurantmanager.api.domain.exception.ValidationException;

public class UserType {

    private final Long id;
    private final String name;
    private final String observation;

    public UserType(final Long id, final String name, final String observation) {
        this.id = id;
        this.name = name;
        this.observation = observation;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getObservation() {
        return observation;
    }

    public void validate() {
        if (name == null || name.isBlank()) {
            throw new ValidationException("name", name, "User type name is required and must not be blank");
        }
    }
}

