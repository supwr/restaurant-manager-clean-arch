package com.restaurantmanager.api.application.usecase.menuitem.update;

import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.model.MenuItem;

import java.util.Objects;
import java.util.UUID;

public class UpdateMenuItemUseCase {

	private final MenuItemGateway menuItemGateway;

	public UpdateMenuItemUseCase(final MenuItemGateway menuItemGateway) {
		this.menuItemGateway = Objects.requireNonNull(menuItemGateway);
	}

	public MenuItem execute(final UUID restaurantUuid, final UUID menuItemUuid, final MenuItem menuItem) {
		Objects.requireNonNull(restaurantUuid);
		Objects.requireNonNull(menuItemUuid);
		Objects.requireNonNull(menuItem);

		final MenuItem existing = menuItemGateway.findByUuidAndRestaurantUuid(menuItemUuid, restaurantUuid)
			.orElseThrow(() -> new EntityNotFoundException("MenuItem", menuItemUuid.toString()));

		final MenuItem updated = new MenuItem(
			existing.getId(),
			existing.getUuid(),
			existing.getRestaurantId(),
			menuItem.getName(),
			menuItem.getDescription(),
			menuItem.getPrice(),
			menuItem.getLocalOnly(),
			menuItem.getPhotoPath()
		);
		updated.validate();


		return menuItemGateway.save(updated);
	}
}

