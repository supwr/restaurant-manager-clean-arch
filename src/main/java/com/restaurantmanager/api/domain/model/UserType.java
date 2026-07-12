package com.restaurantmanager.api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Domain model for UserType.
 * Represents a classification for users (e.g., Restaurant Owner, Customer).
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserType {

    private Long id;
    private String name;
    private String observation;

    /**
     * Validates the UserType business rules.
     * @throws com.restaurantmanager.api.domain.exception.ValidationException if validation fails
     */
    public void validate() {
        if (name == null || name.isBlank()) {
            throw new com.restaurantmanager.api.domain.exception.ValidationException(
                "name", name, "User type name is required and must not be blank"
            );
        }
    }
}

