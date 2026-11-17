package com.splitsync.data.local.mapper;

import com.splitsync.data.local.entity.GroupEntity;
import com.splitsync.domain.model.Group;
import com.splitsync.domain.type.GroupType;
import com.splitsync.domain.valueobject.CurrencyCode;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.UserId;

public final class GroupMapper {

    private GroupMapper() {
    }

    public static GroupEntity toEntity(Group group) {
        GroupEntity entity = new GroupEntity();
        entity.groupId = group.groupId().value();
        entity.name = group.name();
        entity.groupType = group.groupType().name();
        entity.description = group.description();
        entity.currency = group.currency().value();
        entity.createdBy = group.createdBy().value();
        entity.createdAt = group.createdAt();
        entity.updatedAt = group.updatedAt();
        return entity;
    }

    public static Group toDomain(GroupEntity entity) {
        return Group.restore(
                GroupId.of(entity.groupId),
                entity.name,
                GroupType.valueOf(entity.groupType),
                entity.description,
                CurrencyCode.of(entity.currency),
                UserId.of(entity.createdBy),
                entity.createdAt,
                entity.updatedAt);
    }
}
