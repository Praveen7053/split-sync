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
        tableName = "settlement",
        foreignKeys = @ForeignKey(
                entity = GroupEntity.class,
                parentColumns = "group_id",
                childColumns = "group_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index("group_id"))
public class SettlementEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "settlement_id")
    public String settlementId;

    @NonNull
    @ColumnInfo(name = "group_id")
    public String groupId;

    @NonNull
    @ColumnInfo(name = "paid_by")
    public String paidBy;

    @NonNull
    @ColumnInfo(name = "paid_to")
    public String paidTo;

    @NonNull
    @ColumnInfo(name = "amount")
    public String amount;

    @NonNull
    @ColumnInfo(name = "currency")
    public String currency;

    @NonNull
    @ColumnInfo(name = "created_by")
    public String createdBy;

    @NonNull
    @ColumnInfo(name = "settlement_date")
    public LocalDate settlementDate;

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
