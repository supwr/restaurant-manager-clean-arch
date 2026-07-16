package com.restaurantmanager.api.infrastructure.persistence.mapper;

import com.restaurantmanager.api.domain.model.User;
import com.restaurantmanager.api.domain.model.UserType;
import com.restaurantmanager.api.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserPersistenceMapper {

    @Mapping(target = "typeId", expression = "java(resolveTypeId(user))")
    UserEntity toEntity(User user);

    default Long resolveTypeId(final User user) {
        if (user == null || user.getUserType() == null) {
            return 1L;
        }
        return user.getUserType().getId();
    }

    default User toDomain(final UserEntity entity) {
        if (entity == null) {
            return null;
        }

        final UserType userType = mapUserType(entity.getType());

        return User.createWithType(
            entity.getId(),
            entity.getUuid(),
            entity.getName(),
            entity.getEmail(),
            entity.getLogin(),
            entity.getActive(),
            userType,
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    default UserType mapUserType(final com.restaurantmanager.api.infrastructure.persistence.entity.UserTypeEntity entity) {
        if (entity == null) {
            return null;
        }
        return new UserType(entity.getId(), entity.getUuid(), entity.getName());
    }
}

