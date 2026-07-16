package com.restaurantmanager.api.application.usecase.restaurant.delete;

import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;

import java.util.Objects;
import java.util.UUID;

public class DeleteRestaurantUseCase {

	private final RestaurantGateway restaurantGateway;

	public DeleteRestaurantUseCase(final RestaurantGateway restaurantGateway) {
		this.restaurantGateway = Objects.requireNonNull(restaurantGateway);
	}

	public void execute(final UUID uuid) {
		Objects.requireNonNull(uuid);

		if (!restaurantGateway.existsByUuid(uuid)) {
			throw new EntityNotFoundException("Restaurant", uuid.toString());
		}

		restaurantGateway.deleteByUuid(uuid);
	}
}
