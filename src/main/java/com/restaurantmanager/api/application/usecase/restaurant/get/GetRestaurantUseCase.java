package com.restaurantmanager.api.application.usecase.restaurant.get;

import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.model.Restaurant;

import java.util.Objects;
import java.util.UUID;

public class GetRestaurantUseCase {

	private final RestaurantGateway restaurantGateway;

	public GetRestaurantUseCase(final RestaurantGateway restaurantGateway) {
		this.restaurantGateway = Objects.requireNonNull(restaurantGateway, "restaurantGateway must not be null");
	}

	public Restaurant execute(final UUID uuid) {
		Objects.requireNonNull(uuid, "uuid must not be null");
		return restaurantGateway.findByUuid(uuid)
			.orElseThrow(() -> new EntityNotFoundException("Restaurant", uuid.toString()));
	}
}
