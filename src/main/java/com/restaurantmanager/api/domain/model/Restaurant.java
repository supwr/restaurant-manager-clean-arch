package com.restaurantmanager.api.domain.model;

import com.restaurantmanager.api.domain.exception.ValidationException;
import java.util.UUID;

public class Restaurant {

    private final Long id;
    private final UUID uuid;
    private final String name;
    private final String address;
    private final String cuisineType;
    private final String openingHours;
    private final Long ownerUserId;

    public Restaurant(
            final Long id,
            final String name,
            final String address,
            final String cuisineType,
            final String openingHours,
            final Long ownerUserId
    ) {
        this.id = id;
        this.uuid = null;
        this.name = name;
        this.address = address;
        this.cuisineType = cuisineType;
        this.openingHours = openingHours;
        this.ownerUserId = ownerUserId;
    }

    public Restaurant(
            final Long id,
            final UUID uuid,
            final String name,
            final String address,
            final String cuisineType,
            final String openingHours,
            final Long ownerUserId
    ) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.address = address;
        this.cuisineType = cuisineType;
        this.openingHours = openingHours;
        this.ownerUserId = ownerUserId;
    }

    public Long getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public Long getOwnerUserId() {
        return ownerUserId;
    }

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

