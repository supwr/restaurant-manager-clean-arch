package com.restaurantmanager.api.infrastructure.web.mapper;

import com.restaurantmanager.api.domain.model.UserType;
import com.restaurantmanager.api.infrastructure.web.dto.UserTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * MapStruct mapper to convert between UserType (domain) and UserTypeDTO (HTTP).
 * Handles the translation between domain models and HTTP request/response DTOs.
 */
@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserTypeWebMapper {

    /**
     * Maps a UserTypeDTO (request/response) to a UserType domain model.
     * @param dto the DTO
     * @return the domain model
     */
    UserType toDomain(UserTypeDTO dto);

    /**
     * Maps a UserType domain model to a UserTypeDTO (response).
     * @param domain the domain model
     * @return the DTO
     */
    UserTypeDTO toDTO(UserType domain);
}

