package com.restaurantmanager.api.application.usecase.usertype.create;

import com.restaurantmanager.api.application.gateway.UserTypeGateway;
import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.model.UserType;

import java.util.Objects;

public class CreateUserTypeUseCase {

	private final UserTypeGateway userTypeGateway;

	public CreateUserTypeUseCase(final UserTypeGateway userTypeGateway) {
		this.userTypeGateway = Objects.requireNonNull(userTypeGateway, "userTypeGateway must not be null");
	}

	public UserType execute(final UserType userType) {
		Objects.requireNonNull(userType, "userType must not be null");
		userType.validate();

		if (userTypeGateway.existsByName(userType.getName())) {
			throw new ValidationException("name", userType.getName(),
				String.format("A user type with name '%s' already exists", userType.getName()));
		}

		return userTypeGateway.save(userType);
	}
}
