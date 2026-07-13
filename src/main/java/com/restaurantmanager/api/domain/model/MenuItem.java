package com.restaurantmanager.api.domain.model;

import com.restaurantmanager.api.domain.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public record MenuItem (
        private Long id;
        private Long restaurantId;
        private String name;
        private String description;
        private BigDecimal price;
        private Boolean localOnly;
        private String photoPath;
) {



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

