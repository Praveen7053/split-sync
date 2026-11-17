package com.splitsync.data.local.mapper;

import com.splitsync.data.local.entity.ExpenseSplitEntity;
import com.splitsync.domain.model.ExpenseSplit;
import com.splitsync.domain.type.SplitType;
import com.splitsync.domain.valueobject.ExpenseId;
import com.splitsync.domain.valueobject.ExpenseSplitId;
import com.splitsync.domain.valueobject.Money;
import com.splitsync.domain.valueobject.UserId;

import java.math.BigDecimal;

public final class ExpenseSplitMapper {

    private ExpenseSplitMapper() {
    }

    public static ExpenseSplitEntity toEntity(ExpenseSplit split) {
        ExpenseSplitEntity entity = new ExpenseSplitEntity();
        entity.expenseSplitId = split.expenseSplitId().value();
        entity.expenseId = split.expenseId().value();
        entity.userId = split.userId().value();
        entity.splitType = split.splitType().name();
        entity.shareValue = split.shareValue().toPlainString();
        entity.allocatedAmount = split.allocatedAmount().toString();
        entity.createdAt = split.createdAt();
        entity.updatedAt = split.updatedAt();
        return entity;
    }

    public static ExpenseSplit toDomain(ExpenseSplitEntity entity) {
        return ExpenseSplit.restore(
                ExpenseSplitId.of(entity.expenseSplitId),
                ExpenseId.of(entity.expenseId),
                UserId.of(entity.userId),
                SplitType.valueOf(entity.splitType),
                new BigDecimal(entity.shareValue),
                Money.of(entity.allocatedAmount),
                entity.createdAt,
                entity.updatedAt);
    }
}
