package com.restaurantmanager.api.application.usecase.menuitem.get;

import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.model.MenuItem;

import java.util.Objects;
import java.util.UUID;

public class GetMenuItemUseCase {

	private final MenuItemGateway menuItemGateway;
	private final RestaurantGateway restaurantGateway;

	public GetMenuItemUseCase(final MenuItemGateway menuItemGateway, final RestaurantGateway restaurantGateway) {
		this.menuItemGateway = Objects.requireNonNull(menuItemGateway, "menuItemGateway must not be null");
		this.restaurantGateway = Objects.requireNonNull(restaurantGateway, "restaurantGateway must not be null");
	}

	public MenuItem execute(final UUID restaurantUuid, final UUID menuItemUuid) {
		Objects.requireNonNull(restaurantUuid, "restaurantUuid must not be null");
		Objects.requireNonNull(menuItemUuid, "menuItemUuid must not be null");

		final Long restaurantId = restaurantGateway.findByUuid(restaurantUuid)
			.orElseThrow(() -> new EntityNotFoundException("Restaurant", restaurantUuid.toString()))
			.getId();

		final MenuItem menuItem = menuItemGateway.findByUuid(menuItemUuid)
			.orElseThrow(() -> new EntityNotFoundException("MenuItem", menuItemUuid.toString()));

		if (!restaurantId.equals(menuItem.getRestaurantId())) {
			throw new EntityNotFoundException("MenuItem", menuItemUuid.toString());
		}


		return menuItem;
	}
}
