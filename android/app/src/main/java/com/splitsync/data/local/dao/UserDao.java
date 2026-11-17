package com.splitsync.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.splitsync.data.local.entity.UserEntity;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(UserEntity user);

    @Update
    void update(UserEntity user);

    @Query("SELECT * FROM user WHERE user_id = :userId LIMIT 1")
    UserEntity findById(String userId);

    @Query("SELECT * FROM user LIMIT 1")
    UserEntity findFirst();
}
