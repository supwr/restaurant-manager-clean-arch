package com.restaurantmanager.api.domain.model;

import java.time.Instant;
import java.util.UUID;

public class User {

    public static final String OWNER_TYPE = "OWNER";
    public static final String CUSTOMER_TYPE = "CUSTOMER";

    protected Long id;
    protected UUID uuid;
    protected String name;
    protected String email;
    protected String login;
    protected Boolean active;
    protected String password;
    protected Address address;
    protected Instant createdAt;
    protected Instant updatedAt;

    protected User(final Long id, final UUID uuid, final String name, final String email, final String login, final Boolean active, final String password, final Address address, final Instant createdAt, final Instant updatedAt) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.login = login;
        this.active = active;
        this.password = password;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getLogin() {
        return login;
    }

    public Boolean getActive() {
        return active;
    }

    public String getPassword() {
        return password;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }
}
