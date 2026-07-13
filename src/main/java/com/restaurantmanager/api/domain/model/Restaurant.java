package com.restaurantmanager.api.domain.model;

import com.restaurantmanager.api.domain.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {

    private Long id;
    private String name;
    private String address;
    private String cuisineType;
    private String openingHours;
    private Long ownerUserId;

    public void validate() {
        if (name == null || name.isBlank()) {
            throw new ValidationException("name", name, "Restaurant name is required");
        }
        if (address == null || address.isBlank()) {
            throw new ValidationException("address", address, "Address is required");
        }
        if (cuisineType == null || cuisineType.isBlank()) {
            throw new ValidationException("cuisineType", cuisineType, "Cuisine type is required");
        }
        if (openingHours == null || openingHours.isBlank()) {
            throw new ValidationException("openingHours", openingHours, "Opening hours are required");
        }
        if (ownerUserId == null) {
            throw new ValidationException("ownerUserId", null, "Owner user id is required");
        }
    }
}

