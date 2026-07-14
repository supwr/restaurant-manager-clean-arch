package com.restaurantmanager.api.application.gateway;

import com.restaurantmanager.api.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserGateway {

	User save(User user);

	Optional<User> findById(Long id);

	Optional<User> findByUuid(UUID uuid);

	Optional<User> findByEmailIgnoreCase(String email);

	Optional<User> findByLoginIgnoreCase(String login);

	List<User> findAll();

	void deleteById(Long id);

	boolean existsById(Long id);
}
