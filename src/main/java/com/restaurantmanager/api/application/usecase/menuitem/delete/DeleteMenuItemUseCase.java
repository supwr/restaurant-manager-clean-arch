package com.restaurantmanager.api.application.usecase.menuitem.delete;

import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;

import java.util.Objects;
import java.util.UUID;

public class DeleteMenuItemUseCase {

	private final MenuItemGateway menuItemGateway;
	private final RestaurantGateway restaurantGateway;

	public DeleteMenuItemUseCase(final MenuItemGateway menuItemGateway, final RestaurantGateway restaurantGateway) {
		this.menuItemGateway = Objects.requireNonNull(menuItemGateway);
		this.restaurantGateway = Objects.requireNonNull(restaurantGateway);
	}

	public void execute(final UUID restaurantUuid, final UUID menuItemUuid) {
		Objects.requireNonNull(restaurantUuid);
		Objects.requireNonNull(menuItemUuid);

		final Long restaurantId = restaurantGateway.findByUuid(restaurantUuid)
			.orElseThrow(() -> new EntityNotFoundException("Restaurant", restaurantUuid.toString()))
			.getId();

		final var menuItem = menuItemGateway.findByUuid(menuItemUuid)
			.orElseThrow(() -> new EntityNotFoundException("MenuItem", menuItemUuid.toString()));

		if (!restaurantId.equals(menuItem.getRestaurantId())) {
			throw new EntityNotFoundException("MenuItem", menuItemUuid.toString());
		}

		menuItemGateway.deleteByUuid(menuItemUuid);
	}
}
