package com.restaurantmanager.api.application.usecase.user.update;

import com.restaurantmanager.api.application.gateway.UserGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.model.User;

import java.util.Objects;
import java.util.UUID;

public class UpdateUserUseCase {

	private final UserGateway userGateway;

	public UpdateUserUseCase(final UserGateway userGateway) {
		this.userGateway = Objects.requireNonNull(userGateway, "userGateway must not be null");
	}

	public User execute(final UUID uuid, final User user) {
		Objects.requireNonNull(uuid, "uuid must not be null");
		Objects.requireNonNull(user, "user must not be null");

		final User existing = userGateway.findByUuid(uuid)
			.orElseThrow(() -> new EntityNotFoundException("User", uuid.toString()));

		validateUpdatableFields(user);

		if (user.getEmail() != null && !user.getEmail().equalsIgnoreCase(existing.getEmail())
			&& userGateway.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
			throw new ValidationException("email", user.getEmail(), "Email already exists");
		}

		if (user.getLogin() != null && !user.getLogin().equalsIgnoreCase(existing.getLogin())
			&& userGateway.findByLoginIgnoreCase(user.getLogin()).isPresent()) {
			throw new ValidationException("login", user.getLogin(), "Login already exists");
		}

		final User updated = new User(
			existing.getId(),
			existing.getUuid(),
			user.getName() != null ? user.getName() : existing.getName(),
			user.getEmail() != null ? user.getEmail() : existing.getEmail(),
			user.getLogin() != null ? user.getLogin() : existing.getLogin(),
			existing.getActive(),
			existing.getUserType(),
			existing.getCreatedAt(),
			existing.getUpdatedAt()
		);

		return userGateway.save(updated);
	}

	private void validateUpdatableFields(final User user) {
		if (user.getName() != null && user.getName().isBlank()) {
			throw new ValidationException("name", user.getName(), "User name must not be blank");
		}
		if (user.getEmail() != null && user.getEmail().isBlank()) {
			throw new ValidationException("email", user.getEmail(), "User email must not be blank");
		}
		if (user.getLogin() != null && user.getLogin().isBlank()) {
			throw new ValidationException("login", user.getLogin(), "User login must not be blank");
		}
	}
}

