package com.splitsync.data.local.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.splitsync.data.local.converter.RoomTypeConverters;
import com.splitsync.data.local.dao.DeviceDao;
import com.splitsync.data.local.dao.ExpenseDao;
import com.splitsync.data.local.dao.ExpenseSplitDao;
import com.splitsync.data.local.dao.GroupDao;
import com.splitsync.data.local.dao.MembershipDao;
import com.splitsync.data.local.dao.SettlementDao;
import com.splitsync.data.local.dao.UserDao;
import com.splitsync.data.local.entity.DeviceEntity;
import com.splitsync.data.local.entity.ExpenseEntity;
import com.splitsync.data.local.entity.ExpenseSplitEntity;
import com.splitsync.data.local.entity.GroupEntity;
import com.splitsync.data.local.entity.MembershipEntity;
import com.splitsync.data.local.entity.SettlementEntity;
import com.splitsync.data.local.entity.UserEntity;

/**
 * Local Room database for offline-first SplitSync data.
 * Schema version 1 is the V1a baseline.
 */
@Database(
        entities = {
                UserEntity.class,
                DeviceEntity.class,
                GroupEntity.class,
                MembershipEntity.class,
                ExpenseEntity.class,
                ExpenseSplitEntity.class,
                SettlementEntity.class
        },
        version = 1,
        exportSchema = true)
@TypeConverters(RoomTypeConverters.class)
public abstract class SplitSyncDatabase extends RoomDatabase {

    public static final String NAME = "splitsync.db";

    public abstract UserDao userDao();

    public abstract DeviceDao deviceDao();

    public abstract GroupDao groupDao();

    public abstract MembershipDao membershipDao();

    public abstract ExpenseDao expenseDao();

    public abstract ExpenseSplitDao expenseSplitDao();

    public abstract SettlementDao settlementDao();
}
