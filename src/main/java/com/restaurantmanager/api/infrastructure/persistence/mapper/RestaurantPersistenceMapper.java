package com.restaurantmanager.api.infrastructure.persistence.mapper;

import com.restaurantmanager.api.domain.model.Restaurant;
import com.restaurantmanager.api.infrastructure.persistence.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantPersistenceMapper {

    default Restaurant toDomain(final RestaurantEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Restaurant(
            entity.getId(),
            entity.getUuid(),
            entity.getName(),
            entity.getAddress(),
            entity.getCuisineType(),
            entity.getOpeningHours(),
            entity.getOwnerUserId()
        );
    }

    default RestaurantEntity toEntity(final Restaurant domain) {
        if (domain == null) {
            return null;
        }
        final RestaurantEntity entity = new RestaurantEntity();
        entity.setId(domain.getId());
        entity.setUuid(domain.getUuid());
        entity.setName(domain.getName());
        entity.setAddress(domain.getAddress());
        entity.setCuisineType(domain.getCuisineType());
        entity.setOpeningHours(domain.getOpeningHours());
        entity.setOwnerUserId(domain.getOwnerUserId());
        return entity;
    }
}

