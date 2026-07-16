package com.restaurantmanager.api.application.usecase.user.delete;

import com.restaurantmanager.api.application.gateway.UserGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;

import java.util.Objects;
import java.util.UUID;

public class DeleteUserUseCase {

	private final UserGateway userGateway;

	public DeleteUserUseCase(final UserGateway userGateway) {
		this.userGateway = Objects.requireNonNull(userGateway, "userGateway must not be null");
	}

	public void execute(final UUID uuid) {
		Objects.requireNonNull(uuid, "uuid must not be null");
		final Long id = userGateway.findByUuid(uuid)
			.map(user -> user.getId())
			.orElseThrow(() -> new EntityNotFoundException("User", uuid.toString()));
		userGateway.deleteById(id);
	}
}

