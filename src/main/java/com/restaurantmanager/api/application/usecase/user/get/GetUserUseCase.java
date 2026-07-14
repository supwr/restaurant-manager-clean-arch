package com.restaurantmanager.api.application.usecase.user.get;

import com.restaurantmanager.api.application.gateway.UserGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.model.User;

import java.util.Objects;

public class GetUserUseCase {

	private final UserGateway userGateway;

	public GetUserUseCase(final UserGateway userGateway) {
		this.userGateway = Objects.requireNonNull(userGateway, "userGateway must not be null");
	}

	public User execute(final Long id) {
		Objects.requireNonNull(id, "id must not be null");
		return userGateway.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("User", id.toString()));
	}
}
