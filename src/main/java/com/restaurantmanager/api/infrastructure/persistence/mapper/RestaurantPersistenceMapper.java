package com.restaurantmanager.api.infrastructure.persistence.mapper;

import com.restaurantmanager.api.domain.model.Restaurant;
import com.restaurantmanager.api.infrastructure.persistence.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantPersistenceMapper {

    Restaurant toDomain(RestaurantEntity entity);

    RestaurantEntity toEntity(Restaurant domain);
}

