package com.splitsync.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.Instant;

@Entity(
        tableName = "expense_split",
        foreignKeys = @ForeignKey(
                entity = ExpenseEntity.class,
                parentColumns = "expense_id",
                childColumns = "expense_id",
                onDelete = ForeignKey.CASCADE),
        indices = {
                @Index("expense_id"),
                @Index("user_id")
        })
public class ExpenseSplitEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "expense_split_id")
    public String expenseSplitId;

    @NonNull
    @ColumnInfo(name = "expense_id")
    public String expenseId;

    @NonNull
    @ColumnInfo(name = "user_id")
    public String userId;

    @NonNull
    @ColumnInfo(name = "split_type")
    public String splitType;

    // Exact decimal text for percentage/shares/exact inputs.
    @NonNull
    @ColumnInfo(name = "share_value")
    public String shareValue;

    @NonNull
    @ColumnInfo(name = "allocated_amount")
    public String allocatedAmount;

    @NonNull
    @ColumnInfo(name = "created_at")
    public Instant createdAt;

    @NonNull
    @ColumnInfo(name = "updated_at")
    public Instant updatedAt;
}
