package com.restaurantmanager.api.infrastructure.persistence.repository;

import com.restaurantmanager.api.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUuid(UUID uuid);

    Optional<UserEntity> findByEmailIgnoreCase(String email);

    Optional<UserEntity> findByLoginIgnoreCase(String login);

    boolean existsByUuid(UUID uuid);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByLoginIgnoreCase(String login);
}

