package com.splitsync.application;

import com.splitsync.data.local.database.SplitSyncDatabase;
import com.splitsync.data.repository.LocalRepositoryFactory;

/**
 * Facade that wires application services to local repositories.
 */
public final class ApplicationServices {

    private final ProfileService profileService;
    private final GroupService groupService;
    private final ExpenseService expenseService;
    private final BalanceService balanceService;
    private final SettlementService settlementService;

    public ApplicationServices(SplitSyncDatabase database) {
        LocalRepositoryFactory repositories = new LocalRepositoryFactory(database);
        this.profileService = new ProfileService(repositories.users(), repositories.devices());
        this.groupService = new GroupService(profileService, repositories.users(), repositories.groups());
        this.expenseService = new ExpenseService(profileService, repositories.groups(), repositories.expenses());
        this.balanceService = new BalanceService(profileService, repositories.groups(), repositories.expenses(), repositories.settlements());
        this.settlementService = new SettlementService(profileService, repositories.groups(), repositories.settlements());
    }

    public ProfileService profiles() {
        return profileService;
    }

    public GroupService groups() {
        return groupService;
    }

    public ExpenseService expenses() {
        return expenseService;
    }

    public BalanceService balances() {
        return balanceService;
    }

    public SettlementService settlements() {
        return settlementService;
    }
}
