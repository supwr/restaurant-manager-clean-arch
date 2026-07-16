package com.restaurantmanager.api.infrastructure.web.controller;

import com.restaurantmanager.api.RestaurantsApi;
import com.restaurantmanager.api.application.usecase.restaurant.create.CreateRestaurantUseCase;
import com.restaurantmanager.api.application.usecase.restaurant.delete.DeleteRestaurantUseCase;
import com.restaurantmanager.api.application.usecase.restaurant.get.GetRestaurantUseCase;
import com.restaurantmanager.api.application.usecase.restaurant.list.ListRestaurantsUseCase;
import com.restaurantmanager.api.application.usecase.restaurant.update.UpdateRestaurantUseCase;
import com.restaurantmanager.api.domain.model.Restaurant;
import com.restaurantmanager.api.application.gateway.UserGateway;
import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.infrastructure.web.mapper.RestaurantMapper;
import com.restaurantmanager.api.model.RelatedUser;
import java.util.UUID;
import com.restaurantmanager.api.domain.model.Pagination;
import com.restaurantmanager.api.model.RestaurantRequest;
import com.restaurantmanager.api.model.RestaurantResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class RestaurantController implements RestaurantsApi {

    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final GetRestaurantUseCase getRestaurantUseCase;
    private final ListRestaurantsUseCase listRestaurantsUseCase;
    private final UpdateRestaurantUseCase updateRestaurantUseCase;
    private final DeleteRestaurantUseCase deleteRestaurantUseCase;
    private final RestaurantGateway restaurantGateway;
    private final UserGateway userGateway;
    private final RestaurantMapper restaurantMapper;

    public RestaurantController(
        final CreateRestaurantUseCase createRestaurantUseCase,
        final GetRestaurantUseCase getRestaurantUseCase,
        final ListRestaurantsUseCase listRestaurantsUseCase,
        final UpdateRestaurantUseCase updateRestaurantUseCase,
        final DeleteRestaurantUseCase deleteRestaurantUseCase,
        final RestaurantGateway restaurantGateway,
        final UserGateway userGateway,
        final RestaurantMapper restaurantMapper
    ) {
        this.createRestaurantUseCase = Objects.requireNonNull(createRestaurantUseCase);
        this.getRestaurantUseCase = Objects.requireNonNull(getRestaurantUseCase);
        this.listRestaurantsUseCase = Objects.requireNonNull(listRestaurantsUseCase);
        this.updateRestaurantUseCase = Objects.requireNonNull(updateRestaurantUseCase);
        this.deleteRestaurantUseCase = Objects.requireNonNull(deleteRestaurantUseCase);
        this.restaurantGateway = Objects.requireNonNull(restaurantGateway);
        this.userGateway = Objects.requireNonNull(userGateway);
        this.restaurantMapper = Objects.requireNonNull(restaurantMapper);
    }

    @Override
    public ResponseEntity<RestaurantResponse> createRestaurant(@Valid final RestaurantRequest restaurantRequest) {
        final Long ownerUserId = resolveOwnerUserId(restaurantRequest.getOwner().getId());
        final Restaurant created = createRestaurantUseCase.execute(restaurantMapper.map(ownerUserId, restaurantRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @Override
    public ResponseEntity<Void> deleteRestaurant(final UUID id) {
        deleteRestaurantUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<RestaurantResponse> getRestaurantById(final UUID id) {
        return ResponseEntity.ok(toResponse(getRestaurantUseCase.execute(id)));
    }

    @Override
    public ResponseEntity<List<RestaurantResponse>> listRestaurants(final Integer page, final Integer size) {
        final var pagination = new Pagination(page, size, "name");

        return ResponseEntity.ok(listRestaurantsUseCase.execute(pagination).getContent().stream().map(this::toResponse).toList());
    }

    @Override
    public ResponseEntity<RestaurantResponse> updateRestaurant(final UUID id, @Valid final RestaurantRequest restaurantRequest) {
        final Long ownerUserId = resolveOwnerUserId(restaurantRequest.getOwner().getId());
        final Restaurant updated = updateRestaurantUseCase.execute(id, restaurantMapper.map(ownerUserId, restaurantRequest));
        return ResponseEntity.ok(toResponse(updated));
    }

    private RestaurantResponse toResponse(final Restaurant restaurant) {
        final var ownerUser = userGateway.findById(restaurant.getOwnerUserId())
            .orElseThrow(() -> new EntityNotFoundException("User", restaurant.getOwnerUserId().toString()))
            ;
        final RelatedUser relatedUser = new RelatedUser();
        relatedUser.setId(ownerUser.getUuid());
        relatedUser.setName(ownerUser.getName());
        return restaurantMapper.map(restaurant, relatedUser);
    }

    private Long resolveOwnerUserId(final UUID ownerUserUuid) {
        return userGateway.findByUuid(ownerUserUuid)
            .orElseThrow(() -> new EntityNotFoundException("User", ownerUserUuid.toString()))
            .getId();
    }
}
