package com.splitsync.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.splitsync.data.local.entity.ExpenseSplitEntity;

import java.util.List;

@Dao
public interface ExpenseSplitDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertAll(List<ExpenseSplitEntity> splits);

    @Query("SELECT * FROM expense_split WHERE expense_id = :expenseId ORDER BY user_id ASC")
    List<ExpenseSplitEntity> findByExpenseId(String expenseId);

    @Query("SELECT * FROM expense_split WHERE expense_id IN (:expenseIds) ORDER BY expense_id ASC, user_id ASC")
    List<ExpenseSplitEntity> findByExpenseIds(List<String> expenseIds);

    @Query("DELETE FROM expense_split WHERE expense_id = :expenseId")
    void deleteByExpenseId(String expenseId);
}
