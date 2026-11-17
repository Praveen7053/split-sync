package com.splitsync.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.splitsync.data.local.entity.SettlementEntity;

import java.util.List;

@Dao
public interface SettlementDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(SettlementEntity settlement);

    @Update
    void update(SettlementEntity settlement);

    @Query("SELECT * FROM settlement WHERE settlement_id = :settlementId LIMIT 1")
    SettlementEntity findById(String settlementId);

    @Query("SELECT * FROM settlement WHERE group_id = :groupId ORDER BY settlement_date DESC, created_at DESC")
    List<SettlementEntity> findByGroupId(String groupId);

    @Query("DELETE FROM settlement WHERE settlement_id = :settlementId")
    void deleteById(String settlementId);
}
