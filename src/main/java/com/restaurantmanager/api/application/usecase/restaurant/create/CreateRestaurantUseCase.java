package com.restaurantmanager.api.application.usecase.restaurant.create;

import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.domain.model.Restaurant;

import java.util.Objects;

public class CreateRestaurantUseCase {

	private final RestaurantGateway restaurantGateway;

	public CreateRestaurantUseCase(final RestaurantGateway restaurantGateway) {
		this.restaurantGateway = Objects.requireNonNull(restaurantGateway, "restaurantGateway must not be null");
	}

	public Restaurant execute(final Restaurant restaurant) {
		Objects.requireNonNull(restaurant, "restaurant must not be null");
		restaurant.validate();
		return restaurantGateway.save(restaurant);
	}
}
