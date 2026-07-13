package com.restaurantmanager.api.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Owner extends User {

    private Owner(final Long id, final UUID uuid, final String name, final String email, final String login, final Boolean active, final String password, final Address address, final Instant createdAt, final Instant updatedAt) {
        super(id, uuid, name, email, login, active, password, address, createdAt, updatedAt);
    }

    public static Owner create(final Long id, final UUID uuid, final String name, final String email, final String login, final Boolean active, final String password, final Address address, final Instant createdAt, final Instant updatedAt) {
        return new Owner(id, uuid, name, email, login, active, password, address, createdAt, updatedAt);
    }
}
