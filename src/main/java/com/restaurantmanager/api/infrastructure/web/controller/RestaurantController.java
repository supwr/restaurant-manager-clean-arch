package com.restaurantmanager.api.infrastructure.web.controller;

import com.restaurantmanager.api.RestaurantsApi;
import com.restaurantmanager.api.application.usecase.restaurant.create.CreateRestaurantUseCase;
import com.restaurantmanager.api.application.usecase.restaurant.delete.DeleteRestaurantUseCase;
import com.restaurantmanager.api.application.usecase.restaurant.get.GetRestaurantUseCase;
import com.restaurantmanager.api.application.usecase.restaurant.list.ListRestaurantsUseCase;
import com.restaurantmanager.api.application.usecase.restaurant.update.UpdateRestaurantUseCase;
import com.restaurantmanager.api.domain.model.Restaurant;
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

    public RestaurantController(
        final CreateRestaurantUseCase createRestaurantUseCase,
        final GetRestaurantUseCase getRestaurantUseCase,
        final ListRestaurantsUseCase listRestaurantsUseCase,
        final UpdateRestaurantUseCase updateRestaurantUseCase,
        final DeleteRestaurantUseCase deleteRestaurantUseCase
    ) {
        this.createRestaurantUseCase = Objects.requireNonNull(createRestaurantUseCase);
        this.getRestaurantUseCase = Objects.requireNonNull(getRestaurantUseCase);
        this.listRestaurantsUseCase = Objects.requireNonNull(listRestaurantsUseCase);
        this.updateRestaurantUseCase = Objects.requireNonNull(updateRestaurantUseCase);
        this.deleteRestaurantUseCase = Objects.requireNonNull(deleteRestaurantUseCase);
    }

    @Override
    public ResponseEntity<RestaurantResponse> createRestaurant(@Valid final RestaurantRequest restaurantRequest) {
        final Restaurant created = createRestaurantUseCase.execute(toDomain(null, restaurantRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @Override
    public ResponseEntity<Void> deleteRestaurant(final Long id) {
        deleteRestaurantUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<RestaurantResponse> getRestaurantById(final Long id) {
        return ResponseEntity.ok(toResponse(getRestaurantUseCase.execute(id)));
    }

    @Override
    public ResponseEntity<List<RestaurantResponse>> listRestaurants(final Integer page, final Integer size) {
        final int p = page == null ? 0 : page;
        final int s = size == null ? 20 : size;
        return ResponseEntity.ok(listRestaurantsUseCase.execute(new Pagination(p, s, "id")).getContent().stream().map(this::toResponse).toList());
    }

    @Override
    public ResponseEntity<RestaurantResponse> updateRestaurant(final Long id, @Valid final RestaurantRequest restaurantRequest) {
        return ResponseEntity.ok(toResponse(updateRestaurantUseCase.execute(id, toDomain(id, restaurantRequest))));
    }

    private Restaurant toDomain(final Long id, final RestaurantRequest request) {
        return new Restaurant(
            id,
            request.getName(),
            request.getAddress(),
            request.getCuisineType(),
            request.getOpeningHours(),
            request.getOwnerUserId()
        );
    }

    private RestaurantResponse toResponse(final Restaurant restaurant) {
        final RestaurantResponse response = new RestaurantResponse();
        response.setId(restaurant.getId());
        response.setName(restaurant.getName());
        response.setAddress(restaurant.getAddress());
        response.setCuisineType(restaurant.getCuisineType());
        response.setOpeningHours(restaurant.getOpeningHours());
        response.setOwnerUserId(restaurant.getOwnerUserId());
        return response;
    }
}

