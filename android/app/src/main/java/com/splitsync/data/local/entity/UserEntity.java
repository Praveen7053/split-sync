package com.splitsync.data.local.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Instant;

@Entity(tableName = "user")
public class UserEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_id")
    public String userId;

    @NonNull
    @ColumnInfo(name = "display_name")
    public String displayName;

    @Nullable
    @ColumnInfo(name = "phone_number")
    public String phoneNumber;

    @Nullable
    @ColumnInfo(name = "email")
    public String email;

    @Nullable
    @ColumnInfo(name = "profile_photo_path")
    public String profilePhotoPath;

    @NonNull
    @ColumnInfo(name = "created_at")
    public Instant createdAt;

    @NonNull
    @ColumnInfo(name = "updated_at")
    public Instant updatedAt;
}
