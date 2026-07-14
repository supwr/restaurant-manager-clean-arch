package com.restaurantmanager.api.application.usecase.menuitem.update;

import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.model.MenuItem;

import java.util.Objects;

public class UpdateMenuItemUseCase {

	private final MenuItemGateway menuItemGateway;
	private final RestaurantGateway restaurantGateway;

	public UpdateMenuItemUseCase(
		final MenuItemGateway menuItemGateway,
		final RestaurantGateway restaurantGateway
	) {
		this.menuItemGateway = Objects.requireNonNull(menuItemGateway, "menuItemGateway must not be null");
		this.restaurantGateway = Objects.requireNonNull(restaurantGateway, "restaurantGateway must not be null");
	}

	public MenuItem execute(final Long restaurantId, final Long id, final MenuItem menuItem) {
		Objects.requireNonNull(restaurantId, "restaurantId must not be null");
		Objects.requireNonNull(id, "id must not be null");
		Objects.requireNonNull(menuItem, "menuItem must not be null");

		if (!restaurantGateway.existsById(restaurantId)) {
			throw new EntityNotFoundException("Restaurant", restaurantId.toString());
		}

		final MenuItem existing = menuItemGateway.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("MenuItem", id.toString()));

		if (!restaurantId.equals(existing.getRestaurantId())) {
			throw new EntityNotFoundException("MenuItem", id.toString());
		}

		menuItem.validate();

		final MenuItem updated = new MenuItem(
			id,
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

