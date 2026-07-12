package com.restaurantmanager.api.infrastructure.persistence.repository;

import com.restaurantmanager.api.infrastructure.persistence.entity.UserTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for UserType persistence.
 * Provides CRUD operations and custom queries for UserTypeEntity.
 */
@Repository
public interface UserTypeRepository extends JpaRepository<UserTypeEntity, Long> {

    /**
     * Finds a user type by its name.
     * @param name the user type name
     * @return an Optional containing the user type if found
     */
    Optional<UserTypeEntity> findByName(String name);

    /**
     * Checks if a user type with the given name already exists.
     * @param name the user type name
     * @return true if a user type with this name exists, false otherwise
     */
    boolean existsByName(String name);
}

