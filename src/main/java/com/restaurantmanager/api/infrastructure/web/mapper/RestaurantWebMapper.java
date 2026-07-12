package com.restaurantmanager.api.infrastructure.web.mapper;

import com.restaurantmanager.api.domain.model.Restaurant;
import com.restaurantmanager.api.infrastructure.web.dto.RestaurantDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantWebMapper {

    Restaurant toDomain(RestaurantDTO dto);

    RestaurantDTO toDTO(Restaurant domain);
}

