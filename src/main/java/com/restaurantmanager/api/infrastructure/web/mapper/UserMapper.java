package com.restaurantmanager.api.infrastructure.web.mapper;

import com.restaurantmanager.api.model.UserResponse;
import com.restaurantmanager.api.domain.model.Address;
import com.restaurantmanager.api.domain.model.User;

import com.restaurantmanager.api.model.UserType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "uuid", source = "uuid")
    @Mapping(target = "createdAt", expression = "java(user.getCreatedAt() == null ? null : java.time.OffsetDateTime.ofInstant(user.getCreatedAt(), java.time.ZoneOffset.UTC))")
    @Mapping(target = "lastModifiedAt", expression = "java(user.getUpdatedAt() == null ? null : java.time.OffsetDateTime.ofInstant(user.getUpdatedAt(), java.time.ZoneOffset.UTC))")
    @Mapping(target = "type", expression = "java(resolveType(user))")
    UserResponse map(User user);

    default UserType resolveType(final User user) {
        if (user == null) {
            return null;
        }

        com.restaurantmanager.api.model.UserType result = new com.restaurantmanager.api.model.UserType();
        final String simpleName = user.getClass().getSimpleName();
        switch (simpleName) {
            case "Owner" -> result.setName(User.OWNER_TYPE);
            case "Customer" -> result.setName(User.CUSTOMER_TYPE);
            default -> throw new IllegalArgumentException("Unsupported user subtype: " + user.getClass().getName());
        }

        return result;
    }

    default com.restaurantmanager.api.model.Address map(final Address address) {
        if (address == null) {
            return null;
        }
        return new com.restaurantmanager.api.model.Address(
                address.street(),
                address.number() == null ? null : String.valueOf(address.number()),
                address.city(),
                address.zipCode()
        );
    }
}
