package com.splitsync.data.local.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.Instant;

@Entity(
        tableName = "groups",
        foreignKeys = @ForeignKey(
                entity = UserEntity.class,
                parentColumns = "user_id",
                childColumns = "created_by",
                onDelete = ForeignKey.RESTRICT),
        indices = @Index("created_by"))
public class GroupEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "group_id")
    public String groupId;

    @NonNull
    @ColumnInfo(name = "name")
    public String name;

    @NonNull
    @ColumnInfo(name = "group_type")
    public String groupType;

    @Nullable
    @ColumnInfo(name = "description")
    public String description;

    @NonNull
    @ColumnInfo(name = "currency")
    public String currency;

    @NonNull
    @ColumnInfo(name = "created_by")
    public String createdBy;

    @NonNull
    @ColumnInfo(name = "created_at")
    public Instant createdAt;

    @NonNull
    @ColumnInfo(name = "updated_at")
    public Instant updatedAt;
}
