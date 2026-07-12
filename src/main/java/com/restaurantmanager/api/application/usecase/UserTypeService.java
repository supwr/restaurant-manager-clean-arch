package com.restaurantmanager.api.application.usecase;

import com.restaurantmanager.api.application.port.UserTypePersistencePort;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.model.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Application service for UserType use cases.
 * Contains business logic for user type CRUD operations.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserTypeService {

    private final UserTypePersistencePort persistencePort;

    /**
     * Creates a new user type.
     * @param userType the user type to create
     * @return the created user type with generated ID
     * @throws ValidationException if the user type data is invalid
     * @throws ValidationException if a user type with the same name already exists
     */
    public UserType create(UserType userType) {
        // Validate business rules
        userType.validate();

        // Check if name is already in use
        if (persistencePort.existsByName(userType.getName())) {
            throw new ValidationException(
                "name",
                userType.getName(),
                String.format("A user type with name '%s' already exists", userType.getName())
            );
        }

        return persistencePort.save(userType);
    }

    /**
     * Retrieves a user type by ID.
     * @param id the user type ID
     * @return the user type
     * @throws EntityNotFoundException if the user type is not found
     */
    @Transactional(readOnly = true)
    public UserType getById(Long id) {
        return persistencePort.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("UserType", id.toString()));
    }

    /**
     * Lists all user types.
     * @return a list of all user types
     */
    @Transactional(readOnly = true)
    public List<UserType> listAll() {
        return persistencePort.findAll();
    }

    /**
     * Updates an existing user type.
     * @param id the user type ID
     * @param userType the updated user type data
     * @return the updated user type
     * @throws EntityNotFoundException if the user type is not found
     * @throws ValidationException if the updated data is invalid
     */
    public UserType update(Long id, UserType userType) {
        // Verify user type exists
        UserType existing = persistencePort.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("UserType", id.toString()));

        // Validate business rules
        userType.validate();

        // Check if new name is already in use by another user type
        if (!existing.getName().equals(userType.getName()) &&
            persistencePort.existsByName(userType.getName())) {
            throw new ValidationException(
                "name",
                userType.getName(),
                String.format("A user type with name '%s' already exists", userType.getName())
            );
        }

        // Update the user type
        UserType updated = UserType.builder()
            .id(id)
            .name(userType.getName())
            .observation(userType.getObservation())
            .build();

        return persistencePort.save(updated);
    }

    /**
     * Deletes a user type.
     * @param id the user type ID
     * @throws EntityNotFoundException if the user type is not found
     */
    public void delete(Long id) {
        // Verify user type exists
        if (!persistencePort.existsById(id)) {
            throw new EntityNotFoundException("UserType", id.toString());
        }

        persistencePort.deleteById(id);
    }
}

