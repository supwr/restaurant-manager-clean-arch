package com.restaurantmanager.api.application.usecase.restaurant.list;

import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.domain.model.PageResult;
import com.restaurantmanager.api.domain.model.Pagination;
import com.restaurantmanager.api.domain.model.Restaurant;
import com.restaurantmanager.api.domain.exception.ValidationException;

import java.util.List;
import java.util.Objects;

public class ListRestaurantsUseCase {

	private final RestaurantGateway restaurantGateway;

	public ListRestaurantsUseCase(final RestaurantGateway restaurantGateway) {
		this.restaurantGateway = Objects.requireNonNull(restaurantGateway);
	}

	public PageResult<Restaurant> execute(final Pagination pagination) {
		Objects.requireNonNull(pagination);
		if (pagination.getSize() <= 0) {
			throw new ValidationException("size", pagination.getSize(), "Page size must be greater than zero");
		}

		final List<Restaurant> restaurants = restaurantGateway.findAll();
		final int pageNumber = Math.max(pagination.getPage(), 0);
		final int pageSize = pagination.getSize();
		final int fromIndex = Math.min(pageNumber * pageSize, restaurants.size());
		final int toIndex = Math.min(fromIndex + pageSize, restaurants.size());
		final List<Restaurant> content = fromIndex >= toIndex ? List.of() : List.copyOf(restaurants.subList(fromIndex, toIndex));
		final int totalPages = restaurants.isEmpty() ? 0 : (int) Math.ceil((double) restaurants.size() / pageSize);

		return new PageResult<>(content, pageNumber, pageSize, (long) restaurants.size(), totalPages);
	}
}
