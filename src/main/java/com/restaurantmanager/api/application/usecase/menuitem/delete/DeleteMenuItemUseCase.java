package com.restaurantmanager.api.application.usecase.menuitem.delete;

import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;

import java.util.Objects;

public class DeleteMenuItemUseCase {

	private final MenuItemGateway menuItemGateway;
	private final RestaurantGateway restaurantGateway;

	public DeleteMenuItemUseCase(final MenuItemGateway menuItemGateway, final RestaurantGateway restaurantGateway) {
		this.menuItemGateway = Objects.requireNonNull(menuItemGateway, "menuItemGateway must not be null");
		this.restaurantGateway = Objects.requireNonNull(restaurantGateway, "restaurantGateway must not be null");
	}

	public void execute(final Long restaurantId, final Long id) {
		Objects.requireNonNull(restaurantId, "restaurantId must not be null");
		Objects.requireNonNull(id, "id must not be null");

		if (!restaurantGateway.existsById(restaurantId)) {
			throw new EntityNotFoundException("Restaurant", restaurantId.toString());
		}

		final var menuItem = menuItemGateway.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("MenuItem", id.toString()));

		if (!restaurantId.equals(menuItem.getRestaurantId())) {
			throw new EntityNotFoundException("MenuItem", id.toString());
		}

		menuItemGateway.deleteById(id);
	}
}
