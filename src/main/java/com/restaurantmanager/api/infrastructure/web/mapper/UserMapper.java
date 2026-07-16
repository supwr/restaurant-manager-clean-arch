package com.restaurantmanager.api.infrastructure.web.mapper;

import com.restaurantmanager.api.model.UserResponse;
import com.restaurantmanager.api.domain.model.User;

import com.restaurantmanager.api.model.UserType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "uuid", source = "uuid")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "login", source = "login")
    @Mapping(target = "createdAt", expression = "java(user.getCreatedAt() == null ? null : java.time.OffsetDateTime.ofInstant(user.getCreatedAt(), java.time.ZoneOffset.UTC))")
    @Mapping(target = "lastModifiedAt", expression = "java(user.getUpdatedAt() == null ? null : java.time.OffsetDateTime.ofInstant(user.getUpdatedAt(), java.time.ZoneOffset.UTC))")
    @Mapping(target = "type", expression = "java(mapUserType(user.getUserType()))")
    UserResponse map(User user);


    default UserType mapUserType(final com.restaurantmanager.api.domain.model.UserType domainType) {
        if (domainType == null) {
            return null;
        }
        final UserType responseType = new UserType();
        responseType.setUuid(domainType.getUuid());
        responseType.setName(domainType.getName() != null ? domainType.getName().toUpperCase() : null);
        return responseType;
    }
}
