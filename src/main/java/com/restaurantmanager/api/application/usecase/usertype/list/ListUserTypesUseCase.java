package com.restaurantmanager.api.application.usecase.usertype.list;

import com.restaurantmanager.api.application.gateway.UserTypeGateway;
import com.restaurantmanager.api.domain.model.UserType;

import java.util.List;
import java.util.Objects;

public class ListUserTypesUseCase {

	private final UserTypeGateway userTypeGateway;

	public ListUserTypesUseCase(final UserTypeGateway userTypeGateway) {
		this.userTypeGateway = Objects.requireNonNull(userTypeGateway, "userTypeGateway must not be null");
	}

	public List<UserType> execute() {
		return userTypeGateway.findAll();
	}
}
