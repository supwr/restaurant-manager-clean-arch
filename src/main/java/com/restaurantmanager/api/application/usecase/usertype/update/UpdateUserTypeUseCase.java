package com.restaurantmanager.api.application.usecase.usertype.update;

import com.restaurantmanager.api.application.gateway.UserTypeGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.model.UserType;

import java.util.Objects;

public class UpdateUserTypeUseCase {

	private final UserTypeGateway userTypeGateway;

	public UpdateUserTypeUseCase(final UserTypeGateway userTypeGateway) {
		this.userTypeGateway = Objects.requireNonNull(userTypeGateway, "userTypeGateway must not be null");
	}

	public UserType execute(final Long id, final UserType userType) {
		Objects.requireNonNull(id, "id must not be null");
		Objects.requireNonNull(userType, "userType must not be null");

		final UserType existing = userTypeGateway.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("UserType", id.toString()));

		userType.validate();

		if (!existing.getName().equals(userType.getName()) && userTypeGateway.existsByName(userType.getName())) {
			throw new ValidationException("name", userType.getName(),
				String.format("A user type with name '%s' already exists", userType.getName()));
		}

		final UserType updated = new UserType(id, userType.getName());

		return userTypeGateway.save(updated);
	}
}
