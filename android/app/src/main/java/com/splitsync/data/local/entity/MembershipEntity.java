package com.splitsync.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.Instant;

@Entity(
        tableName = "membership",
        foreignKeys = {
                @ForeignKey(
                        entity = GroupEntity.class,
                        parentColumns = "group_id",
                        childColumns = "group_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(
                        entity = UserEntity.class,
                        parentColumns = "user_id",
                        childColumns = "user_id",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index("group_id"),
                @Index("user_id"),
                @Index(value = {"group_id", "user_id"}, unique = true)
        })
public class MembershipEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "membership_id")
    public String membershipId;

    @NonNull
    @ColumnInfo(name = "group_id")
    public String groupId;

    @NonNull
    @ColumnInfo(name = "user_id")
    public String userId;

    @NonNull
    @ColumnInfo(name = "role")
    public String role;

    @NonNull
    @ColumnInfo(name = "created_at")
    public Instant createdAt;

    @NonNull
    @ColumnInfo(name = "updated_at")
    public Instant updatedAt;
}
