package com.restaurantmanager.api.application.usecase.restaurant.update;

import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.model.Restaurant;

import java.util.Objects;

public class UpdateRestaurantUseCase {

	private final RestaurantGateway restaurantGateway;

	public UpdateRestaurantUseCase(final RestaurantGateway restaurantGateway) {
		this.restaurantGateway = Objects.requireNonNull(restaurantGateway, "restaurantGateway must not be null");
	}

	public Restaurant execute(final Long id, final Restaurant restaurant) {
		Objects.requireNonNull(id, "id must not be null");
		Objects.requireNonNull(restaurant, "restaurant must not be null");

		restaurantGateway.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Restaurant", id.toString()));

		restaurant.validate();

		final Restaurant updated = new Restaurant(
			id,
			restaurant.getName(),
			restaurant.getAddress(),
			restaurant.getCuisineType(),
			restaurant.getOpeningHours(),
			restaurant.getOwnerUserId()
		);

		return restaurantGateway.save(updated);
	}
}
