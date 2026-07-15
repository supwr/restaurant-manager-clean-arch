package com.restaurantmanager.api.infrastructure.web.mapper;

import com.restaurantmanager.api.domain.model.Restaurant;
import com.restaurantmanager.api.model.RestaurantRequest;
import com.restaurantmanager.api.model.RestaurantResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    @Mapping(target = "uuid", source = "uuid")
    RestaurantResponse map(Restaurant restaurant);

    default Restaurant map(final RestaurantRequest request) {
        if (request == null) {
            return null;
        }
        return new Restaurant(
            null,
            request.getName(),
            request.getAddress(),
            request.getCuisineType(),
            request.getOpeningHours(),
            request.getOwnerUserId()
        );
    }

    default Restaurant map(final Long id, final RestaurantRequest request) {
        if (request == null) {
            return null;
        }
        return new Restaurant(
            id,
            request.getName(),
            request.getAddress(),
            request.getCuisineType(),
            request.getOpeningHours(),
            request.getOwnerUserId()
        );
    }
}

