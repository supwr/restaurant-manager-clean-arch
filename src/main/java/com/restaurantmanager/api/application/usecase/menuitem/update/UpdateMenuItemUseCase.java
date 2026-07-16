package com.restaurantmanager.api.application.usecase.menuitem.update;

import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.model.MenuItem;

import java.util.Objects;
import java.util.UUID;

public class UpdateMenuItemUseCase {

	private final MenuItemGateway menuItemGateway;
	private final RestaurantGateway restaurantGateway;

	public UpdateMenuItemUseCase(
		final MenuItemGateway menuItemGateway,
		final RestaurantGateway restaurantGateway
	) {
		this.menuItemGateway = Objects.requireNonNull(menuItemGateway);
		this.restaurantGateway = Objects.requireNonNull(restaurantGateway);
	}

	public MenuItem execute(final UUID restaurantUuid, final UUID menuItemUuid, final MenuItem menuItem) {
		Objects.requireNonNull(restaurantUuid);
		Objects.requireNonNull(menuItemUuid);
		Objects.requireNonNull(menuItem);

		final Long restaurantId = restaurantGateway.findByUuid(restaurantUuid)
			.orElseThrow(() -> new EntityNotFoundException("Restaurant", restaurantUuid.toString()))
			.getId();

		final MenuItem existing = menuItemGateway.findByUuid(menuItemUuid)
			.orElseThrow(() -> new EntityNotFoundException("MenuItem", menuItemUuid.toString()));

		if (!restaurantId.equals(existing.getRestaurantId())) {
			throw new EntityNotFoundException("MenuItem", menuItemUuid.toString());
		}

		menuItem.validate();

		final MenuItem updated = new MenuItem(
			existing.getId(),
			restaurantId,
			menuItem.getName(),
			menuItem.getDescription(),
			menuItem.getPrice(),
			menuItem.getLocalOnly(),
			menuItem.getPhotoPath()
		);


		return menuItemGateway.save(updated);
	}
}

