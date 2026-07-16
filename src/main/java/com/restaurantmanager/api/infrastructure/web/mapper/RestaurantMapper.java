package com.restaurantmanager.api.infrastructure.web.mapper;

import com.restaurantmanager.api.domain.model.Restaurant;
import com.restaurantmanager.api.model.RestaurantRequest;
import com.restaurantmanager.api.model.RestaurantResponse;
import com.restaurantmanager.api.model.RelatedUser;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    default RestaurantResponse map(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }
        RestaurantResponse response = new RestaurantResponse();
        response.setUuid(restaurant.getUuid());
        response.setName(restaurant.getName());
        response.setAddress(restaurant.getAddress());
        response.setCuisineType(restaurant.getCuisineType());
        response.setOpeningHours(restaurant.getOpeningHours());
        return response;
    }

    default RestaurantResponse map(final Restaurant restaurant, final RelatedUser owner) {
        if (restaurant == null) {
            return null;
        }

        final RestaurantResponse response = map(restaurant);
        response.setOwner(owner);
        return response;
    }

    default Restaurant map(final Long ownerUserId, final RestaurantRequest request) {
        if (request == null) {
            return null;
        }
        return new Restaurant(null, request.getName(), request.getAddress(), request.getCuisineType(), request.getOpeningHours(), ownerUserId);
    }

    default Restaurant map(final Long id, final Long ownerUserId, final RestaurantRequest request) {
        if (request == null) {
            return null;
        }
        return new Restaurant(id, request.getName(), request.getAddress(), request.getCuisineType(), request.getOpeningHours(), ownerUserId);
    }
}

