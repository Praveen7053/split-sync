package com.splitsync.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.splitsync.data.local.entity.DeviceEntity;

@Dao
public interface DeviceDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(DeviceEntity device);

    @Update
    void update(DeviceEntity device);

    @Query("SELECT * FROM device WHERE device_id = :deviceId LIMIT 1")
    DeviceEntity findById(String deviceId);

    @Query("SELECT * FROM device WHERE user_id = :userId LIMIT 1")
    DeviceEntity findByUserId(String userId);
}
