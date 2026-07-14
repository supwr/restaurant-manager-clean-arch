package com.restaurantmanager.api.application.usecase.user.delete;

import com.restaurantmanager.api.application.gateway.UserGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;

import java.util.Objects;

public class DeleteUserUseCase {

	private final UserGateway userGateway;

	public DeleteUserUseCase(final UserGateway userGateway) {
		this.userGateway = Objects.requireNonNull(userGateway, "userGateway must not be null");
	}

	public void execute(final Long id) {
		Objects.requireNonNull(id, "id must not be null");

		if (!userGateway.existsById(id)) {
			throw new EntityNotFoundException("User", id.toString());
		}

		userGateway.deleteById(id);
	}
}
