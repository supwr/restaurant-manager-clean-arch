package com.restaurantmanager.api.domain.model;

import com.restaurantmanager.api.domain.exception.ValidationException;

import java.util.UUID;

public class UserType {

	private final Long id;
	private final UUID uuid;
	private final String name;

	public UserType(final Long id, final String name) {
		this.id = id;
		this.uuid = null;
		this.name = name;
	}

	public UserType(final Long id, final UUID uuid, final String name) {
		this.id = id;
		this.uuid = uuid;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}


	public void validate() {
		if (name == null || name.isBlank()) {
			throw new ValidationException("name", name, "User type name is required and must not be blank");
		}
	}
}
