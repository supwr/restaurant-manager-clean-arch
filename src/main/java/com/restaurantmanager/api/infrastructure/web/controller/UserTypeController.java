package com.restaurantmanager.api.infrastructure.web.controller;

import com.restaurantmanager.api.UserTypesApi;
import com.restaurantmanager.api.application.usecase.usertype.create.CreateUserTypeUseCase;
import com.restaurantmanager.api.application.usecase.usertype.delete.DeleteUserTypeUseCase;
import com.restaurantmanager.api.application.usecase.usertype.get.GetUserTypeUseCase;
import com.restaurantmanager.api.application.usecase.usertype.list.ListUserTypesUseCase;
import com.restaurantmanager.api.application.usecase.usertype.update.UpdateUserTypeUseCase;
import com.restaurantmanager.api.domain.model.UserType;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.application.gateway.UserTypeGateway;
import com.restaurantmanager.api.model.UserTypeRequest;
import com.restaurantmanager.api.model.UserTypeResponse;
import com.restaurantmanager.api.infrastructure.web.mapper.UserTypeMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
public class UserTypeController implements UserTypesApi {

    private final CreateUserTypeUseCase createUserTypeUseCase;
    private final GetUserTypeUseCase getUserTypeUseCase;
    private final ListUserTypesUseCase listUserTypesUseCase;
    private final UpdateUserTypeUseCase updateUserTypeUseCase;
    private final DeleteUserTypeUseCase deleteUserTypeUseCase;
    private final UserTypeMapper userTypeMapper;
    private final UserTypeGateway userTypeGateway;

    public UserTypeController(
        final CreateUserTypeUseCase createUserTypeUseCase,
        final GetUserTypeUseCase getUserTypeUseCase,
        final ListUserTypesUseCase listUserTypesUseCase,
        final UpdateUserTypeUseCase updateUserTypeUseCase,
        final DeleteUserTypeUseCase deleteUserTypeUseCase,
        final UserTypeMapper userTypeMapper,
        final UserTypeGateway userTypeGateway
    ) {
        this.createUserTypeUseCase = Objects.requireNonNull(createUserTypeUseCase);
        this.getUserTypeUseCase = Objects.requireNonNull(getUserTypeUseCase);
        this.listUserTypesUseCase = Objects.requireNonNull(listUserTypesUseCase);
        this.updateUserTypeUseCase = Objects.requireNonNull(updateUserTypeUseCase);
        this.deleteUserTypeUseCase = Objects.requireNonNull(deleteUserTypeUseCase);
        this.userTypeMapper = Objects.requireNonNull(userTypeMapper);
        this.userTypeGateway = Objects.requireNonNull(userTypeGateway);
    }

    @Override
    public ResponseEntity<UserTypeResponse> createUserType(@Valid final UserTypeRequest userTypeRequest) {
        final UserType created = createUserTypeUseCase.execute(userTypeMapper.map(userTypeRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(userTypeMapper.map(created));
    }

    @Override
    public ResponseEntity<Void> deleteUserType(final UUID userTypeUuid) {
        final Long userTypeId = userTypeGateway.findByUuid(userTypeUuid)
            .orElseThrow(() -> new EntityNotFoundException("UserType", userTypeUuid.toString()))
            .getId();
        deleteUserTypeUseCase.execute(userTypeId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserTypeResponse> getUserTypeById(final UUID userTypeUuid) {
        final Long userTypeId = userTypeGateway.findByUuid(userTypeUuid)
            .orElseThrow(() -> new EntityNotFoundException("UserType", userTypeUuid.toString()))
            .getId();
        return ResponseEntity.ok(userTypeMapper.map(getUserTypeUseCase.execute(userTypeId)));
    }

    @Override
    public ResponseEntity<List<UserTypeResponse>> listUserTypes() {
        return ResponseEntity.ok(listUserTypesUseCase.execute().stream().map(userTypeMapper::map).toList());
    }

    @Override
    public ResponseEntity<UserTypeResponse> updateUserType(final UUID userTypeUuid, @Valid final UserTypeRequest userTypeRequest) {
        final Long userTypeId = userTypeGateway.findByUuid(userTypeUuid)
            .orElseThrow(() -> new EntityNotFoundException("UserType", userTypeUuid.toString()))
            .getId();
        return ResponseEntity.ok(userTypeMapper.map(updateUserTypeUseCase.execute(userTypeId, userTypeMapper.map(userTypeId, userTypeRequest))));
    }
}
