package com.splitsync.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.splitsync.data.local.entity.GroupEntity;

import java.util.List;

@Dao
public interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(GroupEntity group);

    @Update
    void update(GroupEntity group);

    @Query("SELECT * FROM groups WHERE group_id = :groupId LIMIT 1")
    GroupEntity findById(String groupId);

    @Query("SELECT * FROM groups ORDER BY created_at DESC")
    List<GroupEntity> findAll();

    @Query("DELETE FROM groups WHERE group_id = :groupId")
    void deleteById(String groupId);
}
