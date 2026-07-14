package com.restaurantmanager.api.application.usecase.user.create;

import com.restaurantmanager.api.application.gateway.UserGateway;
import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.model.User;

import java.util.Objects;

public class CreateUserUseCase {

	private final UserGateway userGateway;

	public CreateUserUseCase(final UserGateway userGateway) {
		this.userGateway = Objects.requireNonNull(userGateway, "userGateway must not be null");
	}

	public User execute(final User user) {
		Objects.requireNonNull(user, "user must not be null");
		validateUser(user);

		if (userGateway.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
			throw new ValidationException("email", user.getEmail(), "Email already exists");
		}
		if (userGateway.findByLoginIgnoreCase(user.getLogin()).isPresent()) {
			throw new ValidationException("login", user.getLogin(), "Login already exists");
		}

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
