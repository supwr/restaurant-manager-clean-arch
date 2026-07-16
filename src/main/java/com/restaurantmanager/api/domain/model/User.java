package com.restaurantmanager.api.domain.model;

import java.time.Instant;
import java.util.UUID;

public class User {

    private Long id;
    private UUID uuid;
    private String name;
    private String email;
    private String login;
    private Boolean active;
    private UserType userType;
    private Instant createdAt;
    private Instant updatedAt;

    public User() {
    }

    public User(final Long id, final UUID uuid, final String name, final String email, final String login, final Boolean active, final Instant createdAt, final Instant updatedAt) {
        this(id, uuid, name, email, login, active, null, createdAt, updatedAt);
    }

    public User(final Long id, final UUID uuid, final String name, final String email, final String login, final Boolean active, final UserType userType, final Instant createdAt, final Instant updatedAt) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.login = login;
        this.active = active;
        this.userType = userType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(final Long id, final UUID uuid, final String name, final String email, final String login, final Boolean active, final Instant createdAt, final Instant updatedAt) {
        return new User(id, uuid, name, email, login, active, null, createdAt, updatedAt);
    }

    public static User createWithType(final Long id, final UUID uuid, final String name, final String email, final String login, final Boolean active, final UserType userType, final Instant createdAt, final Instant updatedAt) {
        return new User(id, uuid, name, email, login, active, userType, createdAt, updatedAt);
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

    public UserType getUserType() {
        return userType;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setUuid(final UUID uuid) {
        this.uuid = uuid;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }


    public void setUserType(final UserType userType) {
        this.userType = userType;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
