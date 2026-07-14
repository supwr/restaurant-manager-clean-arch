package com.restaurantmanager.api.application.usecase.user.create;

public record CreateUserCommand(
    String type,
    String name,
    String email,
    String login,
    String password,
    String street,
    Long number,
    String city,
    String zipCode
) {
}

