package com.restaurantmanager.api.infrastructure.web.controller;

import com.restaurantmanager.api.UserTypesApi;
import com.restaurantmanager.api.application.usecase.usertype.create.CreateUserTypeUseCase;
import com.restaurantmanager.api.application.usecase.usertype.delete.DeleteUserTypeUseCase;
import com.restaurantmanager.api.application.usecase.usertype.get.GetUserTypeUseCase;
import com.restaurantmanager.api.application.usecase.usertype.list.ListUserTypesUseCase;
import com.restaurantmanager.api.application.usecase.usertype.update.UpdateUserTypeUseCase;
import com.restaurantmanager.api.domain.model.UserType;
import com.restaurantmanager.api.model.UserTypeRequest;
import com.restaurantmanager.api.model.UserTypeResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class UserTypeController implements UserTypesApi {

    private final CreateUserTypeUseCase createUserTypeUseCase;
    private final GetUserTypeUseCase getUserTypeUseCase;
    private final ListUserTypesUseCase listUserTypesUseCase;
    private final UpdateUserTypeUseCase updateUserTypeUseCase;
    private final DeleteUserTypeUseCase deleteUserTypeUseCase;

    public UserTypeController(
        final CreateUserTypeUseCase createUserTypeUseCase,
        final GetUserTypeUseCase getUserTypeUseCase,
        final ListUserTypesUseCase listUserTypesUseCase,
        final UpdateUserTypeUseCase updateUserTypeUseCase,
        final DeleteUserTypeUseCase deleteUserTypeUseCase
    ) {
        this.createUserTypeUseCase = Objects.requireNonNull(createUserTypeUseCase);
        this.getUserTypeUseCase = Objects.requireNonNull(getUserTypeUseCase);
        this.listUserTypesUseCase = Objects.requireNonNull(listUserTypesUseCase);
        this.updateUserTypeUseCase = Objects.requireNonNull(updateUserTypeUseCase);
        this.deleteUserTypeUseCase = Objects.requireNonNull(deleteUserTypeUseCase);
    }

    @Override
    public ResponseEntity<UserTypeResponse> createUserType(@Valid final UserTypeRequest userTypeRequest) {
        final UserType created = createUserTypeUseCase.execute(toDomain(null, userTypeRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @Override
    public ResponseEntity<Void> deleteUserType(final Long id) {
        deleteUserTypeUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserTypeResponse> getUserTypeById(final Long id) {
        return ResponseEntity.ok(toResponse(getUserTypeUseCase.execute(id)));
    }

    @Override
    public ResponseEntity<List<UserTypeResponse>> listUserTypes() {
        return ResponseEntity.ok(listUserTypesUseCase.execute().stream().map(this::toResponse).toList());
    }

    @Override
    public ResponseEntity<UserTypeResponse> updateUserType(final Long id, @Valid final UserTypeRequest userTypeRequest) {
        return ResponseEntity.ok(toResponse(updateUserTypeUseCase.execute(id, toDomain(id, userTypeRequest))));
    }

    private UserType toDomain(final Long id, final UserTypeRequest request) {
        return new UserType(id, request.getName(), request.getObservation());
    }

    private UserTypeResponse toResponse(final UserType userType) {
        final UserTypeResponse response = new UserTypeResponse();
        response.setId(userType.getId());
        response.setName(userType.getName());
        response.setObservation(userType.getObservation());
        return response;
    }
}

