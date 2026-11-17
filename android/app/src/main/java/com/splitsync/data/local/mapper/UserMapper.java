package com.splitsync.data.local.mapper;

import com.splitsync.data.local.entity.UserEntity;
import com.splitsync.domain.model.User;
import com.splitsync.domain.valueobject.UserId;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.userId = user.userId().value();
        entity.displayName = user.displayName();
        entity.phoneNumber = user.phoneNumber();
        entity.email = user.email();
        entity.profilePhotoPath = user.profilePhotoPath();
        entity.createdAt = user.createdAt();
        entity.updatedAt = user.updatedAt();
        return entity;
    }

    public static User toDomain(UserEntity entity) {
        return User.restore(
                UserId.of(entity.userId),
                entity.displayName,
                entity.phoneNumber,
                entity.email,
                entity.profilePhotoPath,
                entity.createdAt,
                entity.updatedAt);
    }
}
