package com.restaurantmanager.api.infrastructure.persistence.repository;

import com.restaurantmanager.api.infrastructure.persistence.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

	Optional<RestaurantEntity> findByUuid(UUID uuid);

	boolean existsByUuid(UUID uuid);

	void deleteByUuid(UUID uuid);

}

