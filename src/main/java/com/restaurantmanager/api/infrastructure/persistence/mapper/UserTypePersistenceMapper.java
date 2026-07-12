package com.restaurantmanager.api.infrastructure.persistence.mapper;

import com.restaurantmanager.api.domain.model.UserType;
import com.restaurantmanager.api.infrastructure.persistence.entity.UserTypeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * MapStruct mapper to convert between UserType (domain) and UserTypeEntity (persistence).
 * Handles the translation between domain models and JPA entities.
 */
@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserTypePersistenceMapper {

    /**
     * Maps a UserTypeEntity to a UserType domain model.
     * @param entity the persistence entity
     * @return the domain model
     */
    UserType toDomain(UserTypeEntity entity);

    /**
     * Maps a UserType domain model to a UserTypeEntity.
     * @param domain the domain model
     * @return the persistence entity
     */
    UserTypeEntity toEntity(UserType domain);
}

