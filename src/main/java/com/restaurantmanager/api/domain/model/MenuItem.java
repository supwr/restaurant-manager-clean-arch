package com.restaurantmanager.api.domain.model;

import com.restaurantmanager.api.domain.exception.ValidationException;

import java.math.BigDecimal;

public class MenuItem {

    private final Long id;
    private final Long restaurantId;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final Boolean localOnly;
    private final String photoPath;

    public MenuItem(
            final Long id,
            final Long restaurantId,
            final String name,
            final String description,
            final BigDecimal price,
            final Boolean localOnly,
            final String photoPath
    ) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.localOnly = localOnly;
        this.photoPath = photoPath;
    }

    public Long getId() {
        return id;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Boolean getLocalOnly() {
        return localOnly;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void validate() {
        if (restaurantId == null) {
            throw new ValidationException("restaurantId", null, "Restaurant id is required");
        }
        if (name == null || name.isBlank()) {
            throw new ValidationException("name", name, "Menu item name is required");
        }
        if (description == null || description.isBlank()) {
            throw new ValidationException("description", description, "Description is required");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("price", price, "Price must be a positive value");
        }
        if (localOnly == null) {
            throw new ValidationException("localOnly", null, "localOnly flag is required");
        }
        if (photoPath == null) {
            throw new ValidationException("photoPath", null, "Photo path must be present (can be empty)");
        }
    }
}

