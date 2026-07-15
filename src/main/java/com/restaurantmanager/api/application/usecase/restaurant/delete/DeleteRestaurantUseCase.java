package com.restaurantmanager.api.application.usecase.restaurant.delete;

import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;

import java.util.Objects;

public class DeleteRestaurantUseCase {

	private final RestaurantGateway restaurantGateway;

	public DeleteRestaurantUseCase(final RestaurantGateway restaurantGateway) {
		this.restaurantGateway = Objects.requireNonNull(restaurantGateway);
	}

	public void execute(final Long id) {
		Objects.requireNonNull(id);

		if (!restaurantGateway.existsById(id)) {
			throw new EntityNotFoundException("Restaurant", id.toString());
		}

		restaurantGateway.deleteById(id);
	}
}
