package com.restaurantmanager.api.application.usecase.restaurant.update;

import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.model.Restaurant;

import java.util.Objects;
import java.util.UUID;

public class UpdateRestaurantUseCase {

	private final RestaurantGateway restaurantGateway;

	public UpdateRestaurantUseCase(final RestaurantGateway restaurantGateway) {
		this.restaurantGateway = Objects.requireNonNull(restaurantGateway);
	}

	public Restaurant execute(final UUID uuid, final Restaurant restaurant) {
		Objects.requireNonNull(uuid);
		Objects.requireNonNull(restaurant);

		final Restaurant existing = restaurantGateway.findByUuid(uuid)
			.orElseThrow(() -> new EntityNotFoundException("Restaurant", uuid.toString()));

		restaurant.validate();

		final Restaurant updated = new Restaurant(
			existing.getId(),
			restaurant.getName(),
			restaurant.getAddress(),
			restaurant.getCuisineType(),
			restaurant.getOpeningHours(),
			restaurant.getOwnerUserId()
		);


		return restaurantGateway.save(updated);
	}
}
