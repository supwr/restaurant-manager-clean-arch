package com.restaurantmanager.api.application.gateway;

import com.restaurantmanager.api.domain.model.UserType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserTypeGateway {

    UserType save(UserType userType);

    Optional<UserType> findById(Long id);

    Optional<UserType> findByUuid(UUID uuid);

    Optional<UserType> findByName(String name);

    List<UserType> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean existsByUuid(UUID uuid);

    boolean existsByName(String name);
}

