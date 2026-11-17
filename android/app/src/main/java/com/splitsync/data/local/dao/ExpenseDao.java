package com.splitsync.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.splitsync.data.local.entity.ExpenseEntity;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(ExpenseEntity expense);

    @Update
    void update(ExpenseEntity expense);

    @Query("SELECT * FROM expense WHERE expense_id = :expenseId LIMIT 1")
    ExpenseEntity findById(String expenseId);

    @Query("SELECT * FROM expense WHERE group_id = :groupId ORDER BY expense_date DESC, created_at DESC")
    List<ExpenseEntity> findByGroupId(String groupId);

    @Query("DELETE FROM expense WHERE expense_id = :expenseId")
    void deleteById(String expenseId);
}
