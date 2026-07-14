package com.restaurantmanager.api.application.usecase.usertype.delete;

import com.restaurantmanager.api.application.gateway.UserTypeGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;

import java.util.Objects;

public class DeleteUserTypeUseCase {

	private final UserTypeGateway userTypeGateway;

	public DeleteUserTypeUseCase(final UserTypeGateway userTypeGateway) {
		this.userTypeGateway = Objects.requireNonNull(userTypeGateway, "userTypeGateway must not be null");
	}

	public void execute(final Long id) {
		Objects.requireNonNull(id, "id must not be null");

		if (!userTypeGateway.existsById(id)) {
			throw new EntityNotFoundException("UserType", id.toString());
		}

		userTypeGateway.deleteById(id);
	}
}
