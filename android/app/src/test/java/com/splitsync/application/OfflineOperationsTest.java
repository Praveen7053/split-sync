package com.splitsync.application;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.splitsync.data.local.database.SplitSyncDatabase;
import com.splitsync.domain.exception.ValidationException;
import com.splitsync.domain.model.Expense;
import com.splitsync.domain.model.Group;
import com.splitsync.domain.model.Membership;
import com.splitsync.domain.model.User;
import com.splitsync.domain.type.GroupType;
import com.splitsync.domain.type.MembershipRole;
import com.splitsync.domain.type.SplitType;
import com.splitsync.domain.validation.SplitParticipantInput;
import com.splitsync.domain.valueobject.ExpenseId;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.MemberBalance;
import com.splitsync.domain.valueobject.Money;
import com.splitsync.domain.valueobject.UserId;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Proves V1a core flows are offline-first: Room only, no network permission, survives relaunch.
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 35)
public class OfflineOperationsTest {

    private SplitSyncDatabase database;

    @After
    public void tearDown() {
        if (database != null) {
            database.close();
        }
    }

    @Test
    public void manifestDoesNotRequestInternetPermission() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
        String[] permissions = packageInfo.requestedPermissions;
        if (permissions == null) {
            return;
        }
        assertFalse(Arrays.asList(permissions).contains("android.permission.INTERNET"));
    }

    @Test
    public void fullOfflineFlowSurvivesDatabaseRelaunch() {
        Context context = ApplicationProvider.getApplicationContext();
        File dbFile = new File(context.getFilesDir(), "offline-operations-smoke.db");
        if (dbFile.exists()) {
            assertTrue(dbFile.delete());
        }

        database = openFileDatabase(context, dbFile);
        ApplicationServices services = new ApplicationServices(database);

        // Must-work offline flow: profile → group → expense → balances → settlement → edit/delete.
        User owner = services.profiles().createLocalProfile("Praveen", "Pixel");
        Group group = services.groups().createGroup("Goa Trip", GroupType.TRIP, "INR");
        Membership friend = services.groups().addLocalMember(group.groupId(), "Rahul", MembershipRole.MEMBER);

        Expense expense = services.expenses().addExpense(
                group.groupId(),
                "Dinner",
                Money.of("100.00"),
                owner.userId(),
                LocalDate.of(2026, 11, 18),
                SplitType.EQUAL,
                List.of(
                        SplitParticipantInput.equalShare(owner.userId()),
                        SplitParticipantInput.equalShare(friend.userId())));

        Map<UserId, Money> balancesBefore = toMap(services.balances().getGroupBalances(group.groupId()));
        assertEquals(Money.of("50.00"), balancesBefore.get(owner.userId()));
        assertEquals(Money.of("-50.00"), balancesBefore.get(friend.userId()));

        services.settlements().createSettlement(
                group.groupId(),
                friend.userId(),
                owner.userId(),
                Money.of("20.00"),
                LocalDate.of(2026, 11, 19));

        Expense updated = services.expenses().updateExpense(
                expense.expenseId(),
                "Dinner Updated",
                Money.of("120.00"),
                owner.userId(),
                LocalDate.of(2026, 11, 18),
                SplitType.EQUAL,
                List.of(
                        SplitParticipantInput.equalShare(owner.userId()),
                        SplitParticipantInput.equalShare(friend.userId())));
        assertEquals("Dinner Updated", updated.description());

        String groupId = group.groupId().value();
        String expenseId = expense.expenseId().value();
        String ownerId = owner.userId().value();
        String friendId = friend.userId().value();

        // Simulate app relaunch: close DB process handle and open the same file again.
        database.close();
        database = openFileDatabase(context, dbFile);
        ApplicationServices relaunched = new ApplicationServices(database);

        assertEquals("Praveen", relaunched.profiles().getLocalProfile().displayName());
        assertEquals("Goa Trip", relaunched.groups().getGroup(group.groupId()).name());
        assertEquals(2, relaunched.groups().listMembers(group.groupId()).size());
        assertEquals("Dinner Updated", relaunched.expenses().listExpenses(group.groupId()).get(0).description());
        assertEquals(1, relaunched.settlements().listSettlements(group.groupId()).size());

        Map<UserId, Money> balancesAfter = toMap(relaunched.balances().getGroupBalances(group.groupId()));
        // After 120 equal split and 20 settlement from friend → owner:
        // owner paid 120, owes 60 => +60; then receives 20 => +40
        // friend owes 60; pays 20 => -40
        assertEquals(Money.of("40.00"), balancesAfter.get(UserId.of(ownerId)));
        assertEquals(Money.of("-40.00"), balancesAfter.get(UserId.of(friendId)));

        relaunched.expenses().deleteExpense(ExpenseId.of(expenseId));
        assertEquals(0, relaunched.expenses().listExpenses(GroupId.of(groupId)).size());
        assertNotNull(relaunched.profiles().getLocalProfile());
    }

    @Test
    public void invalidOperationsAreRejectedLocallyWithoutNetwork() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, SplitSyncDatabase.class).allowMainThreadQueries().build();
        ApplicationServices services = new ApplicationServices(database);

        assertThrows(ValidationException.class, () -> services.profiles().createLocalProfile(" ", "Pixel"));

        User owner = services.profiles().createLocalProfile("Praveen", "Pixel");
        assertThrows(ValidationException.class, () -> services.groups().createGroup(" ", GroupType.TRIP, "INR"));

        Group group = services.groups().createGroup("Goa Trip", GroupType.TRIP, "INR");
        Membership friend = services.groups().addLocalMember(group.groupId(), "Rahul", MembershipRole.MEMBER);

        List<SplitParticipantInput> badExact = List.of(
                SplitParticipantInput.exact(owner.userId(), new java.math.BigDecimal("30.00")),
                SplitParticipantInput.exact(friend.userId(), new java.math.BigDecimal("30.00")));

        assertThrows(ValidationException.class, () -> services.expenses().addExpense(
                group.groupId(),
                "Bad Exact",
                Money.of("100.00"),
                owner.userId(),
                LocalDate.of(2026, 11, 18),
                SplitType.EXACT,
                badExact));

        assertThrows(ApplicationException.class, () -> services.expenses().addExpense(
                group.groupId(),
                "Stranger Split",
                Money.of("50.00"),
                owner.userId(),
                LocalDate.of(2026, 11, 18),
                SplitType.EQUAL,
                List.of(
                        SplitParticipantInput.equalShare(owner.userId()),
                        SplitParticipantInput.equalShare(UserId.generate()))));
    }

    private static SplitSyncDatabase openFileDatabase(Context context, File dbFile) {
        return Room.databaseBuilder(context, SplitSyncDatabase.class, dbFile.getAbsolutePath())
                .allowMainThreadQueries()
                .build();
    }

    private static Map<UserId, Money> toMap(List<MemberBalance> balances) {
        return balances.stream().collect(Collectors.toMap(MemberBalance::userId, MemberBalance::netAmount));
    }
}
