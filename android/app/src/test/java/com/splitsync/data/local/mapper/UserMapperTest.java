package com.splitsync.data.local.mapper;

import com.splitsync.data.local.entity.UserEntity;
import com.splitsync.domain.model.User;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserMapperTest {

    @Test
    public void roundTripsDomainThroughEntity() {
        User original = User.create("Praveen");
        UserEntity entity = UserMapper.toEntity(original);
        User restored = UserMapper.toDomain(entity);

        assertEquals(original.userId(), restored.userId());
        assertEquals(original.displayName(), restored.displayName());
        assertEquals(original.createdAt(), restored.createdAt());
        assertEquals(original.updatedAt(), restored.updatedAt());
    }
}
