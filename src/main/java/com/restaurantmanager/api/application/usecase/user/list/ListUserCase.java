package com.restaurantmanager.api.application.usecase.user.list;

import com.restaurantmanager.api.application.gateway.UserGateway;
import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.model.PageResult;
import com.restaurantmanager.api.domain.model.Pagination;
import com.restaurantmanager.api.domain.model.User;

import java.util.List;
import java.util.Objects;

public class ListUserCase {

	private final UserGateway userGateway;

	public ListUserCase(final UserGateway userGateway) {
		this.userGateway = Objects.requireNonNull(userGateway, "userGateway must not be null");
	}

	public PageResult<User> execute(final Pagination pagination) {
		Objects.requireNonNull(pagination, "pagination must not be null");
		if (pagination.getSize() <= 0) {
			throw new ValidationException("size", pagination.getSize(), "Page size must be greater than zero");
		}

		final List<User> users = userGateway.findAll();
		final int pageNumber = Math.max(pagination.getPage(), 0);
		final int pageSize = pagination.getSize();
		final int fromIndex = Math.min(pageNumber * pageSize, users.size());
		final int toIndex = Math.min(fromIndex + pageSize, users.size());
		final List<User> content = fromIndex >= toIndex ? List.of() : List.copyOf(users.subList(fromIndex, toIndex));
		final int totalPages = users.isEmpty() ? 0 : (int) Math.ceil((double) users.size() / pageSize);

		return new PageResult<>(content, pageNumber, pageSize, (long) users.size(), totalPages);
	}
}
