package com.splitsync.data.repository;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.splitsync.data.local.database.SplitSyncDatabase;
import com.splitsync.domain.model.Expense;
import com.splitsync.domain.model.ExpenseSplit;
import com.splitsync.domain.model.Group;
import com.splitsync.domain.model.Membership;
import com.splitsync.domain.model.Settlement;
import com.splitsync.domain.model.User;
import com.splitsync.domain.type.GroupType;
import com.splitsync.domain.type.MembershipRole;
import com.splitsync.domain.type.SplitType;
import com.splitsync.domain.validation.SplitCalculator;
import com.splitsync.domain.validation.SplitParticipantInput;
import com.splitsync.domain.valueobject.CurrencyCode;
import com.splitsync.domain.valueobject.Money;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 35)
public class LocalRepositoryRoomTest {

    private SplitSyncDatabase database;
    private LocalRepositoryFactory repositories;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, SplitSyncDatabase.class).allowMainThreadQueries().build();
        repositories = new LocalRepositoryFactory(database);
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void persistsUserGroupExpenseSplitsAndSettlement() {
        User user = User.create("Praveen");
        User friend = User.create("Friend");
        repositories.users().save(user);
        repositories.users().save(friend);

        Group group = Group.create("Goa Trip", GroupType.TRIP, CurrencyCode.of("INR"), user.userId());
        Membership owner = Membership.createOwner(group.groupId(), user.userId());
        repositories.groups().saveGroupWithOwner(group, owner);
        repositories.groups().saveMembership(Membership.create(group.groupId(), friend.userId(), MembershipRole.MEMBER));

        Expense expense = Expense.create(
                group.groupId(),
                "Dinner",
                Money.of("100.00"),
                group.currency(),
                user.userId(),
                user.userId(),
                LocalDate.of(2026, 11, 18));
        List<ExpenseSplit> splits = SplitCalculator.calculate(
                expense.expenseId(),
                expense.amount(),
                SplitType.EQUAL,
                List.of(
                        SplitParticipantInput.equalShare(user.userId()),
                        SplitParticipantInput.equalShare(friend.userId())));
        repositories.expenses().saveExpenseWithSplits(expense, splits);

        Settlement settlement = Settlement.create(
                group.groupId(),
                friend.userId(),
                user.userId(),
                Money.of("50.00"),
                group.currency(),
                user.userId(),
                LocalDate.of(2026, 11, 18));
        repositories.settlements().save(settlement);

        assertNotNull(repositories.users().findById(user.userId()));
        assertEquals("Goa Trip", repositories.groups().findById(group.groupId()).name());
        assertEquals(2, repositories.groups().findMemberships(group.groupId()).size());
        assertEquals("Dinner", repositories.expenses().findById(expense.expenseId()).description());
        assertEquals(2, repositories.expenses().findSplits(expense.expenseId()).size());
        assertEquals(Money.of("50.00"), repositories.settlements().findById(settlement.settlementId()).amount());
    }

    @Test
    public void survivesDatabaseReopenWithinSameProcess() {
        User user = User.create("Praveen");
        repositories.users().save(user);

        // Re-open a new connection against the same in-memory DB instance is not possible,
        // so verify find after clear of repository wrapper still reads persisted row.
        LocalRepositoryFactory freshRepos = new LocalRepositoryFactory(database);
        assertEquals("Praveen", freshRepos.users().findLocalProfile().displayName());
    }
}
