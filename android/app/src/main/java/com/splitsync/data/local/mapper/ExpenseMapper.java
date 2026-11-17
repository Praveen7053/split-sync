package com.splitsync.data.local.mapper;

import com.splitsync.data.local.entity.ExpenseEntity;
import com.splitsync.domain.model.Expense;
import com.splitsync.domain.valueobject.CurrencyCode;
import com.splitsync.domain.valueobject.ExpenseId;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.Money;
import com.splitsync.domain.valueobject.UserId;

public final class ExpenseMapper {

    private ExpenseMapper() {
    }

    public static ExpenseEntity toEntity(Expense expense) {
        ExpenseEntity entity = new ExpenseEntity();
        entity.expenseId = expense.expenseId().value();
        entity.groupId = expense.groupId().value();
        entity.description = expense.description();
        entity.amount = expense.amount().toString();
        entity.currency = expense.currency().value();
        entity.paidBy = expense.paidBy().value();
        entity.createdBy = expense.createdBy().value();
        entity.expenseDate = expense.expenseDate();
        entity.category = expense.category();
        entity.notes = expense.notes();
        entity.createdAt = expense.createdAt();
        entity.updatedAt = expense.updatedAt();
        return entity;
    }

    public static Expense toDomain(ExpenseEntity entity) {
        return Expense.restore(
                ExpenseId.of(entity.expenseId),
                GroupId.of(entity.groupId),
                entity.description,
                Money.of(entity.amount),
                CurrencyCode.of(entity.currency),
                UserId.of(entity.paidBy),
                UserId.of(entity.createdBy),
                entity.expenseDate,
                entity.category,
                entity.notes,
                entity.createdAt,
                entity.updatedAt);
    }
}
