package com.restaurantmanager.api.application.usecase.usertype;

import com.restaurantmanager.api.application.gateway.UserTypeGateway;
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

    private final UserTypeGateway persistencePort;

    public UserType create(UserType userType) {

        userType.validate();

        if (persistencePort.existsByName(userType.getName())) {
            throw new ValidationException(
                "name",
                userType.getName(),
                String.format("A user type with name '%s' already exists", userType.getName())
            );
        }

        return persistencePort.save(userType);
    }

    @Transactional(readOnly = true)
    public UserType getById(Long id) {
        return persistencePort.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("UserType", id.toString()));
    }

    @Transactional(readOnly = true)
    public List<UserType> listAll() {
        return persistencePort.findAll();
    }

    public UserType update(Long id, UserType userType) {

        UserType existing = persistencePort.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("UserType", id.toString()));

        userType.validate();

        if (!existing.getName().equals(userType.getName()) &&
            persistencePort.existsByName(userType.getName())) {
            throw new ValidationException(
                "name",
                userType.getName(),
                String.format("A user type with name '%s' already exists", userType.getName())
            );
        }

        UserType updated = UserType.builder()
            .id(id)
            .name(userType.getName())
            .observation(userType.getObservation())
            .build();

        return persistencePort.save(updated);
    }

    public void delete(Long id) {

        if (!persistencePort.existsById(id)) {
            throw new EntityNotFoundException("UserType", id.toString());
        }

        persistencePort.deleteById(id);
    }
}

