package com.restaurantmanager.api.application.usecase.menuitem.delete;

import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;

import java.util.Objects;
import java.util.UUID;

public class DeleteMenuItemUseCase {

	private final MenuItemGateway menuItemGateway;

	public DeleteMenuItemUseCase(final MenuItemGateway menuItemGateway) {
		this.menuItemGateway = Objects.requireNonNull(menuItemGateway);
	}

	public void execute(final UUID restaurantUuid, final UUID menuItemUuid) {
		Objects.requireNonNull(restaurantUuid);
		Objects.requireNonNull(menuItemUuid);

		final var menuItem = menuItemGateway.findByUuidAndRestaurantUuid(menuItemUuid, restaurantUuid)
			.orElseThrow(() -> new EntityNotFoundException("MenuItem", menuItemUuid.toString()));


		menuItemGateway.deleteByUuid(menuItemUuid);
	}
}
