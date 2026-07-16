package com.restaurantmanager.api.application.usecase.user.create;

import com.restaurantmanager.api.application.gateway.UserGateway;
import com.restaurantmanager.api.application.gateway.UserTypeGateway;
import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.model.User;
import com.restaurantmanager.api.domain.model.UserType;

import java.util.Objects;

public class CreateUserUseCase {

	private final UserGateway userGateway;
	private final UserTypeGateway userTypeGateway;

	public CreateUserUseCase(final UserGateway userGateway, final UserTypeGateway userTypeGateway) {
		this.userGateway = Objects.requireNonNull(userGateway, "userGateway must not be null");
		this.userTypeGateway = Objects.requireNonNull(userTypeGateway, "userTypeGateway must not be null");
	}

	public User execute(final java.util.UUID userTypeUuid, final User user) {
		Objects.requireNonNull(user, "user must not be null");
		Objects.requireNonNull(userTypeUuid, "userTypeUuid must not be null");
		validateUser(user);

		if (userGateway.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
			throw new ValidationException("email", user.getEmail(), "Email already exists");
		}
		if (userGateway.findByLoginIgnoreCase(user.getLogin()).isPresent()) {
			throw new ValidationException("login", user.getLogin(), "Login already exists");
		}

		// Resolve user type by UUID
		final UserType userType = userTypeGateway.findByUuid(userTypeUuid)
				.orElseThrow(() -> new EntityNotFoundException("UserType", userTypeUuid.toString()));

		user.setUserType(userType);

		return userGateway.save(user);
	}

	private void validateUser(final User user) {
		if (user.getName() == null || user.getName().isBlank()) {
			throw new ValidationException("name", user.getName(), "User name is required");
		}
		if (user.getEmail() == null || user.getEmail().isBlank()) {
			throw new ValidationException("email", user.getEmail(), "User email is required");
		}
		if (user.getLogin() == null || user.getLogin().isBlank()) {
			throw new ValidationException("login", user.getLogin(), "User login is required");
		}
	}
}
