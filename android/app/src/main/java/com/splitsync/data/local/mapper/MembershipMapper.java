package com.splitsync.data.local.mapper;

import com.splitsync.data.local.entity.MembershipEntity;
import com.splitsync.domain.model.Membership;
import com.splitsync.domain.type.MembershipRole;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.MembershipId;
import com.splitsync.domain.valueobject.UserId;

public final class MembershipMapper {

    private MembershipMapper() {
    }

    public static MembershipEntity toEntity(Membership membership) {
        MembershipEntity entity = new MembershipEntity();
        entity.membershipId = membership.membershipId().value();
        entity.groupId = membership.groupId().value();
        entity.userId = membership.userId().value();
        entity.role = membership.role().name();
        entity.createdAt = membership.createdAt();
        entity.updatedAt = membership.updatedAt();
        return entity;
    }

    public static Membership toDomain(MembershipEntity entity) {
        return Membership.restore(
                MembershipId.of(entity.membershipId),
                GroupId.of(entity.groupId),
                UserId.of(entity.userId),
                MembershipRole.valueOf(entity.role),
                entity.createdAt,
                entity.updatedAt);
    }
}
