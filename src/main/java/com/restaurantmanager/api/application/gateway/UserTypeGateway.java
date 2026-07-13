package com.restaurantmanager.api.application.gateway;

import com.restaurantmanager.api.domain.model.UserType;

import java.util.List;
import java.util.Optional;

public interface UserTypeGateway {

    UserType save(UserType userType);

    Optional<UserType> findById(Long id);

    Optional<UserType> findByName(String name);

    List<UserType> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean existsByName(String name);
}

