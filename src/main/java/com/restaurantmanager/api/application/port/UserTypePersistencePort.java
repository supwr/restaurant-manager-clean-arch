package com.restaurantmanager.api.application.port;

import com.restaurantmanager.api.domain.model.UserType;

import java.util.List;
import java.util.Optional;

/**
 * Output port for UserType persistence.
 * Defines the interface that the application layer expects from the infrastructure layer
 * for UserType persistence operations.
 */
public interface UserTypePersistencePort {

    /**
     * Saves a user type (create or update).
     * @param userType the user type to save
     * @return the saved user type with generated ID
     */
    UserType save(UserType userType);

    /**
     * Finds a user type by ID.
     * @param id the user type ID
     * @return an Optional containing the user type if found
     */
    Optional<UserType> findById(Long id);

    /**
     * Finds a user type by name.
     * @param name the user type name
     * @return an Optional containing the user type if found
     */
    Optional<UserType> findByName(String name);

    /**
     * Returns all user types.
     * @return a list of all user types
     */
    List<UserType> findAll();

    /**
     * Deletes a user type by ID.
     * @param id the user type ID
     */
    void deleteById(Long id);

    /**
     * Checks if a user type exists by ID.
     * @param id the user type ID
     * @return true if the user type exists, false otherwise
     */
    boolean existsById(Long id);

    /**
     * Checks if a user type with the given name exists.
     * @param name the user type name
     * @return true if a user type with this name exists, false otherwise
     */
    boolean existsByName(String name);
}

