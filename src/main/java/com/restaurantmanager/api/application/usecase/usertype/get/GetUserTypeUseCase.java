package com.restaurantmanager.api.application.usecase.usertype.get;

import com.restaurantmanager.api.application.gateway.UserTypeGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.model.UserType;

import java.util.Objects;

public class GetUserTypeUseCase {

	private final UserTypeGateway userTypeGateway;

	public GetUserTypeUseCase(final UserTypeGateway userTypeGateway) {
		this.userTypeGateway = Objects.requireNonNull(userTypeGateway, "userTypeGateway must not be null");
	}

	public UserType execute(final Long id) {
		Objects.requireNonNull(id, "id must not be null");
		return userTypeGateway.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("UserType", id.toString()));
	}
}
