package com.splitsync.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.splitsync.data.local.entity.MembershipEntity;

import java.util.List;

@Dao
public interface MembershipDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(MembershipEntity membership);

    @Update
    void update(MembershipEntity membership);

    @Query("SELECT * FROM membership WHERE membership_id = :membershipId LIMIT 1")
    MembershipEntity findById(String membershipId);

    @Query("SELECT * FROM membership WHERE group_id = :groupId ORDER BY created_at ASC")
    List<MembershipEntity> findByGroupId(String groupId);

    @Query("SELECT * FROM membership WHERE group_id = :groupId AND user_id = :userId LIMIT 1")
    MembershipEntity findByGroupAndUser(String groupId, String userId);

    @Query("DELETE FROM membership WHERE membership_id = :membershipId")
    void deleteById(String membershipId);
}
