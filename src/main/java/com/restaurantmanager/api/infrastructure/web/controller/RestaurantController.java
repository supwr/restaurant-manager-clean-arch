package com.restaurantmanager.api.infrastructure.web.controller;

import com.restaurantmanager.api.RestaurantsApi;
import com.restaurantmanager.api.application.usecase.restaurant.create.CreateRestaurantUseCase;
import com.restaurantmanager.api.application.usecase.restaurant.delete.DeleteRestaurantUseCase;
import com.restaurantmanager.api.application.usecase.restaurant.get.GetRestaurantUseCase;
import com.restaurantmanager.api.application.usecase.restaurant.list.ListRestaurantsUseCase;
import com.restaurantmanager.api.application.usecase.restaurant.update.UpdateRestaurantUseCase;
import com.restaurantmanager.api.domain.model.Restaurant;
import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.infrastructure.web.mapper.RestaurantMapper;
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
    private final RestaurantMapper restaurantMapper;

    public RestaurantController(
        final CreateRestaurantUseCase createRestaurantUseCase,
        final GetRestaurantUseCase getRestaurantUseCase,
        final ListRestaurantsUseCase listRestaurantsUseCase,
        final UpdateRestaurantUseCase updateRestaurantUseCase,
        final DeleteRestaurantUseCase deleteRestaurantUseCase,
        final RestaurantGateway restaurantGateway,
        final RestaurantMapper restaurantMapper
    ) {
        this.createRestaurantUseCase = Objects.requireNonNull(createRestaurantUseCase);
        this.getRestaurantUseCase = Objects.requireNonNull(getRestaurantUseCase);
        this.listRestaurantsUseCase = Objects.requireNonNull(listRestaurantsUseCase);
        this.updateRestaurantUseCase = Objects.requireNonNull(updateRestaurantUseCase);
        this.deleteRestaurantUseCase = Objects.requireNonNull(deleteRestaurantUseCase);
        this.restaurantGateway = Objects.requireNonNull(restaurantGateway);
        this.restaurantMapper = Objects.requireNonNull(restaurantMapper);
    }

    @Override
    public ResponseEntity<RestaurantResponse> createRestaurant(@Valid final RestaurantRequest restaurantRequest) {
        final Restaurant created = createRestaurantUseCase.execute(restaurantMapper.map(restaurantRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantMapper.map(created));
    }

    @Override
    public ResponseEntity<Void> deleteRestaurant(final UUID id) {
        final Long internalId = restaurantGateway.findByUuid(id)
            .orElseThrow(() -> new EntityNotFoundException("Restaurant", id.toString()))
            .getId();
        deleteRestaurantUseCase.execute(internalId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<RestaurantResponse> getRestaurantById(final UUID id) {
        final var restaurant = restaurantGateway.findByUuid(id)
            .orElseThrow(() -> new EntityNotFoundException("Restaurant", id.toString()));
        return ResponseEntity.ok(restaurantMapper.map(restaurant));
    }

    @Override
    public ResponseEntity<List<RestaurantResponse>> listRestaurants(final Integer page, final Integer size) {
        final int p = page == null ? 0 : page;
        final int s = size == null ? 20 : size;
        return ResponseEntity.ok(listRestaurantsUseCase.execute(new Pagination(p, s, "id")).getContent().stream().map(restaurantMapper::map).toList());
    }

    @Override
    public ResponseEntity<RestaurantResponse> updateRestaurant(final UUID id, @Valid final RestaurantRequest restaurantRequest) {
        final Long internalId = restaurantGateway.findByUuid(id)
            .orElseThrow(() -> new EntityNotFoundException("Restaurant", id.toString()))
            .getId();
        return ResponseEntity.ok(restaurantMapper.map(updateRestaurantUseCase.execute(internalId, restaurantMapper.map(internalId, restaurantRequest))));
    }
}
