package com.restaurantmanager.api.domain.model;

import com.restaurantmanager.api.domain.exception.ValidationException;

public record Address(
        String street,
        Long number,
        String city,
        String zipCode
) {
    public Address {
        if (street == null || street.isEmpty()) {
            throw new ValidationException("Street cannot be null or empty");
        }
        if (number == null || number <= 0) {
            throw new ValidationException("Number must be greater than zero");
        }
        if (city == null || city.isEmpty()) {
            throw new ValidationException("City cannot be null or empty");
        }
        if (zipCode == null || zipCode.isEmpty()) {
            throw new ValidationException("Zip code cannot be null or empty");
        }
    }
}
