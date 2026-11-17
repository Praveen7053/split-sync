package com.splitsync.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.Instant;

@Entity(
        tableName = "device",
        foreignKeys = @ForeignKey(
                entity = UserEntity.class,
                parentColumns = "user_id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index("user_id"))
public class DeviceEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "device_id")
    public String deviceId;

    @NonNull
    @ColumnInfo(name = "user_id")
    public String userId;

    @NonNull
    @ColumnInfo(name = "device_name")
    public String deviceName;

    @NonNull
    @ColumnInfo(name = "created_at")
    public Instant createdAt;

    @NonNull
    @ColumnInfo(name = "updated_at")
    public Instant updatedAt;
}
