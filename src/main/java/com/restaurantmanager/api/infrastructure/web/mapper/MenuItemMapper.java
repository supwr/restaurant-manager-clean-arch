package com.restaurantmanager.api.infrastructure.web.mapper;

import com.restaurantmanager.api.domain.model.MenuItem;
import com.restaurantmanager.api.model.MenuItemRequest;
import com.restaurantmanager.api.model.MenuItemResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {

    MenuItemResponse map(MenuItem menuItem);

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

