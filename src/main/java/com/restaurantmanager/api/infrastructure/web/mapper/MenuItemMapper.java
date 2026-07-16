package com.restaurantmanager.api.infrastructure.web.mapper;

import com.restaurantmanager.api.domain.model.MenuItem;
import com.restaurantmanager.api.model.MenuItemRequest;
import com.restaurantmanager.api.model.MenuItemResponse;
import com.restaurantmanager.api.model.RelatedRestaurant;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {

    default MenuItem map(final MenuItemRequest request) {
        if (request == null) {
            return null;
        }
        return new MenuItem(null, null, request.getName(), request.getDescription(), request.getPrice(), request.getLocalOnly(), request.getPhotoPath());
    }

    default MenuItemResponse map(MenuItem menuItem) {
        if (menuItem == null) {
            return null;
        }
        MenuItemResponse response = new MenuItemResponse();
        response.setUuid(menuItem.getUuid());
        response.setName(menuItem.getName());
        response.setDescription(menuItem.getDescription());
        response.setPrice(menuItem.getPrice());
        response.setLocalOnly(menuItem.getLocalOnly());
        response.setPhotoPath(menuItem.getPhotoPath());
        return response;
    }

    default MenuItemResponse map(final MenuItem menuItem, final RelatedRestaurant restaurant) {
        if (menuItem == null) {
            return null;
        }
        final MenuItemResponse response = map(menuItem);
        response.setRestaurant(restaurant);
        return response;
    }

    default MenuItem map(final Long restaurantId, final MenuItemRequest request) {
        if (request == null) {
            return null;
        }
        return new MenuItem(
            null,
            restaurantId,
            request.getName(),
            request.getDescription(),
            request.getPrice(),
            request.getLocalOnly(),
            request.getPhotoPath()
        );
    }

    default MenuItem map(final Long restaurantId, final Long id, final MenuItemRequest request) {
        if (request == null) {
            return null;
        }
        return new MenuItem(
            id,
            restaurantId,
            request.getName(),
            request.getDescription(),
            request.getPrice(),
            request.getLocalOnly(),
            request.getPhotoPath()
        );
    }
}
