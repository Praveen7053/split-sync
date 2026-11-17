package com.splitsync.data.repository;

import com.splitsync.data.local.database.SplitSyncDatabase;

/**
 * Builds local repositories from one Room database instance.
 */
public final class LocalRepositoryFactory {

    private final SplitSyncDatabase database;

    public LocalRepositoryFactory(SplitSyncDatabase database) {
        this.database = database;
    }

    public UserLocalRepository users() {
        return new UserLocalRepository(database.userDao());
    }

    public DeviceLocalRepository devices() {
        return new DeviceLocalRepository(database.deviceDao());
    }

    public GroupLocalRepository groups() {
        return new GroupLocalRepository(database, database.groupDao(), database.membershipDao());
    }

    public ExpenseLocalRepository expenses() {
        return new ExpenseLocalRepository(database, database.expenseDao(), database.expenseSplitDao());
    }

    public SettlementLocalRepository settlements() {
        return new SettlementLocalRepository(database.settlementDao());
    }
}
