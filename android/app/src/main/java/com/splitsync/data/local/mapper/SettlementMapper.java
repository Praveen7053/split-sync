package com.splitsync.data.local.mapper;

import com.splitsync.data.local.entity.SettlementEntity;
import com.splitsync.domain.model.Settlement;
import com.splitsync.domain.valueobject.CurrencyCode;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.Money;
import com.splitsync.domain.valueobject.SettlementId;
import com.splitsync.domain.valueobject.UserId;

public final class SettlementMapper {

    private SettlementMapper() {
    }

    public static SettlementEntity toEntity(Settlement settlement) {
        SettlementEntity entity = new SettlementEntity();
        entity.settlementId = settlement.settlementId().value();
        entity.groupId = settlement.groupId().value();
        entity.paidBy = settlement.paidBy().value();
        entity.paidTo = settlement.paidTo().value();
        entity.amount = settlement.amount().toString();
        entity.currency = settlement.currency().value();
        entity.createdBy = settlement.createdBy().value();
        entity.settlementDate = settlement.settlementDate();
        entity.notes = settlement.notes();
        entity.createdAt = settlement.createdAt();
        entity.updatedAt = settlement.updatedAt();
        return entity;
    }

    public static Settlement toDomain(SettlementEntity entity) {
        return Settlement.restore(
                SettlementId.of(entity.settlementId),
                GroupId.of(entity.groupId),
                UserId.of(entity.paidBy),
                UserId.of(entity.paidTo),
                Money.of(entity.amount),
                CurrencyCode.of(entity.currency),
                UserId.of(entity.createdBy),
                entity.settlementDate,
                entity.notes,
                entity.createdAt,
                entity.updatedAt);
    }
}
