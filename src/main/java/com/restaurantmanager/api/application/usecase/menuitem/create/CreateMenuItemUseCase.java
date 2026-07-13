package com.restaurantmanager.api.application.usecase.menuitem.create;

import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.model.MenuItem;

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

    public MenuItem execute(final MenuItem menuItem) {
        if (!restaurantGateway.existsById(menuItem.getRestaurantId())) {
            throw new EntityNotFoundException("Restaurant", menuItem.getRestaurantId().toString());
        }

        menuItem.validate();
        return menuItemGateway.save(menuItem);
    }
}
