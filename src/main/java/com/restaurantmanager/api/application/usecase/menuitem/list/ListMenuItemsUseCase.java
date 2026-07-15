package com.restaurantmanager.api.application.usecase.menuitem.list;

import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.model.MenuItem;
import com.restaurantmanager.api.domain.model.PageResult;
import com.restaurantmanager.api.domain.model.Pagination;

import java.util.List;
import java.util.Objects;

public class ListMenuItemsUseCase {

	private final MenuItemGateway menuItemGateway;
	private final RestaurantGateway restaurantGateway;

	public ListMenuItemsUseCase(
		final MenuItemGateway menuItemGateway,
		final RestaurantGateway restaurantGateway
	) {
		this.menuItemGateway = Objects.requireNonNull(menuItemGateway);
		this.restaurantGateway = Objects.requireNonNull(restaurantGateway);
	}

	public PageResult<MenuItem> execute(final Long restaurantId, final Pagination pagination) {
		Objects.requireNonNull(restaurantId);
		Objects.requireNonNull(pagination);

		if (!restaurantGateway.existsById(restaurantId)) {
			throw new EntityNotFoundException("Restaurant", restaurantId.toString());
		}
		if (pagination.getSize() <= 0) {
			throw new ValidationException("size", pagination.getSize(), "Page size must be greater than zero");
		}

		final List<MenuItem> menuItems = menuItemGateway.findByRestaurantId(restaurantId);
		final int pageNumber = Math.max(pagination.getPage(), 0);
		final int pageSize = pagination.getSize();
		final int fromIndex = Math.min(pageNumber * pageSize, menuItems.size());
		final int toIndex = Math.min(fromIndex + pageSize, menuItems.size());
		final List<MenuItem> content = fromIndex >= toIndex ? List.of() : List.copyOf(menuItems.subList(fromIndex, toIndex));
		final int totalPages = menuItems.isEmpty() ? 0 : (int) Math.ceil((double) menuItems.size() / pageSize);

		return new PageResult<>(content, pageNumber, pageSize, (long) menuItems.size(), totalPages);
	}
}
