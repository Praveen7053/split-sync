package com.splitsync.data.repository;

import androidx.room.RoomDatabase;

import com.splitsync.data.local.dao.ExpenseDao;
import com.splitsync.data.local.dao.ExpenseSplitDao;
import com.splitsync.data.local.entity.ExpenseEntity;
import com.splitsync.data.local.entity.ExpenseSplitEntity;
import com.splitsync.data.local.mapper.ExpenseMapper;
import com.splitsync.data.local.mapper.ExpenseSplitMapper;
import com.splitsync.domain.model.Expense;
import com.splitsync.domain.model.ExpenseSplit;
import com.splitsync.domain.valueobject.ExpenseId;
import com.splitsync.domain.valueobject.GroupId;

import java.util.ArrayList;
import java.util.List;

/**
 * Room-backed expense persistence. Expense and splits are written in one transaction.
 */
public class ExpenseLocalRepository {

    private final RoomDatabase database;
    private final ExpenseDao expenseDao;
    private final ExpenseSplitDao expenseSplitDao;

    public ExpenseLocalRepository(RoomDatabase database, ExpenseDao expenseDao, ExpenseSplitDao expenseSplitDao) {
        this.database = database;
        this.expenseDao = expenseDao;
        this.expenseSplitDao = expenseSplitDao;
    }

    public void saveExpenseWithSplits(Expense expense, List<ExpenseSplit> splits) {
        database.runInTransaction(() -> {
            expenseDao.insert(ExpenseMapper.toEntity(expense));
            List<ExpenseSplitEntity> splitEntities = new ArrayList<>();
            for (ExpenseSplit split : splits) {
                splitEntities.add(ExpenseSplitMapper.toEntity(split));
            }
            expenseSplitDao.insertAll(splitEntities);
        });
    }

    public void replaceExpenseWithSplits(Expense expense, List<ExpenseSplit> splits) {
        database.runInTransaction(() -> {
            expenseSplitDao.deleteByExpenseId(expense.expenseId().value());
            expenseDao.update(ExpenseMapper.toEntity(expense));
            List<ExpenseSplitEntity> splitEntities = new ArrayList<>();
            for (ExpenseSplit split : splits) {
                splitEntities.add(ExpenseSplitMapper.toEntity(split));
            }
            expenseSplitDao.insertAll(splitEntities);
        });
    }

    public void deleteExpense(ExpenseId expenseId) {
        // Splits cascade-delete via FK, but explicit delete keeps intent clear.
        database.runInTransaction(() -> {
            expenseSplitDao.deleteByExpenseId(expenseId.value());
            expenseDao.deleteById(expenseId.value());
        });
    }

    public Expense findById(ExpenseId expenseId) {
        ExpenseEntity entity = expenseDao.findById(expenseId.value());
        return entity == null ? null : ExpenseMapper.toDomain(entity);
    }

    public List<Expense> findByGroupId(GroupId groupId) {
        List<Expense> expenses = new ArrayList<>();
        for (ExpenseEntity entity : expenseDao.findByGroupId(groupId.value())) {
            expenses.add(ExpenseMapper.toDomain(entity));
        }
        return expenses;
    }

    public List<ExpenseSplit> findSplits(ExpenseId expenseId) {
        List<ExpenseSplit> splits = new ArrayList<>();
        for (ExpenseSplitEntity entity : expenseSplitDao.findByExpenseId(expenseId.value())) {
            splits.add(ExpenseSplitMapper.toDomain(entity));
        }
        return splits;
    }

    public List<ExpenseSplit> findSplitsForGroup(GroupId groupId) {
        List<Expense> expenses = findByGroupId(groupId);
        if (expenses.isEmpty()) {
            return List.of();
        }
        List<String> expenseIds = new ArrayList<>();
        for (Expense expense : expenses) {
            expenseIds.add(expense.expenseId().value());
        }
        List<ExpenseSplit> splits = new ArrayList<>();
        for (ExpenseSplitEntity entity : expenseSplitDao.findByExpenseIds(expenseIds)) {
            splits.add(ExpenseSplitMapper.toDomain(entity));
        }
        return splits;
    }
}
