package com.restaurantmanager.api.application.usecase.menuitem.create;

import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.model.MenuItem;

import java.util.Objects;
import java.util.UUID;

public class CreateMenuItemUseCase {

    private final MenuItemGateway menuItemGateway;
    private final RestaurantGateway restaurantGateway;

    public CreateMenuItemUseCase(
        final MenuItemGateway menuItemGateway,
        final RestaurantGateway restaurantGateway
    ) {
        this.menuItemGateway = menuItemGateway;
        this.restaurantGateway = restaurantGateway;
    }

    public MenuItem execute(final UUID restaurantUuid, final MenuItem menuItem) {
        Objects.requireNonNull(restaurantUuid);
        Objects.requireNonNull(menuItem);

        final Long restaurantId = restaurantGateway.findByUuid(restaurantUuid)
            .orElseThrow(() -> new EntityNotFoundException("Restaurant", restaurantUuid.toString()))
            .getId();

        final MenuItem itemToSave = new MenuItem(
            null,
            restaurantId,
            menuItem.getName(),
            menuItem.getDescription(),
            menuItem.getPrice(),
            menuItem.getLocalOnly(),
            menuItem.getPhotoPath()
        );

        itemToSave.validate();
        return menuItemGateway.save(itemToSave);
    }
}
