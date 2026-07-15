package com.restaurantmanager.api.application.usecase.menuitem.delete;

import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;

import java.util.Objects;

public class DeleteMenuItemUseCase {

	private final MenuItemGateway menuItemGateway;
	private final RestaurantGateway restaurantGateway;

	public DeleteMenuItemUseCase(final MenuItemGateway menuItemGateway, final RestaurantGateway restaurantGateway) {
		this.menuItemGateway = Objects.requireNonNull(menuItemGateway);
		this.restaurantGateway = Objects.requireNonNull(restaurantGateway);
	}

	public void execute(final Long restaurantId, final Long id) {
		Objects.requireNonNull(restaurantId);
		Objects.requireNonNull(id);

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
