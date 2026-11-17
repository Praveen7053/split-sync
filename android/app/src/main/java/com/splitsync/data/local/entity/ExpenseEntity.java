package com.splitsync.data.local.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.Instant;
import java.time.LocalDate;

@Entity(
        tableName = "expense",
        foreignKeys = @ForeignKey(
                entity = GroupEntity.class,
                parentColumns = "group_id",
                childColumns = "group_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index("group_id"))
public class ExpenseEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "expense_id")
    public String expenseId;

    @NonNull
    @ColumnInfo(name = "group_id")
    public String groupId;

    @NonNull
    @ColumnInfo(name = "description")
    public String description;

    // Exact money text such as "100.00" — never a floating-point type.
    @NonNull
    @ColumnInfo(name = "amount")
    public String amount;

    @NonNull
    @ColumnInfo(name = "currency")
    public String currency;

    @NonNull
    @ColumnInfo(name = "paid_by")
    public String paidBy;

    @NonNull
    @ColumnInfo(name = "created_by")
    public String createdBy;

    @NonNull
    @ColumnInfo(name = "expense_date")
    public LocalDate expenseDate;

    @Nullable
    @ColumnInfo(name = "category")
    public String category;

    @Nullable
    @ColumnInfo(name = "notes")
    public String notes;

    @NonNull
    @ColumnInfo(name = "created_at")
    public Instant createdAt;

    @NonNull
    @ColumnInfo(name = "updated_at")
    public Instant updatedAt;
}
