package com.splitsync.application;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.splitsync.data.local.database.SplitSyncDatabase;
import com.splitsync.domain.exception.ValidationException;
import com.splitsync.domain.model.Expense;
import com.splitsync.domain.model.Group;
import com.splitsync.domain.model.Membership;
import com.splitsync.domain.model.Settlement;
import com.splitsync.domain.model.User;
import com.splitsync.domain.type.GroupType;
import com.splitsync.domain.type.MembershipRole;
import com.splitsync.domain.type.SplitType;
import com.splitsync.domain.validation.SplitParticipantInput;
import com.splitsync.domain.valueobject.MemberBalance;
import com.splitsync.domain.valueobject.Money;
import com.splitsync.domain.valueobject.UserId;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 35)
public class ApplicationServicesTest {

    private SplitSyncDatabase database;
    private ApplicationServices services;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, SplitSyncDatabase.class).allowMainThreadQueries().build();
        services = new ApplicationServices(database);
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void createLocalProfileAndDevice() {
        User user = services.profiles().createLocalProfile("Praveen", "Pixel");
        assertNotNull(user.userId());
        assertEquals("Praveen", services.profiles().getLocalProfile().displayName());
        assertNotNull(services.profiles().ensureDevice(user, "Pixel"));
    }

    @Test
    public void createLocalProfileRejectsBlankName() {
        assertThrows(ValidationException.class, () -> services.profiles().createLocalProfile("  ", "Pixel"));
    }

    @Test
    public void createGroupAddsCreatorAsOwner() {
        services.profiles().createLocalProfile("Praveen", "Pixel");
        Group group = services.groups().createGroup("Goa Trip", GroupType.TRIP, "INR");

        List<Membership> members = services.groups().listMembers(group.groupId());
        assertEquals(1, members.size());
        assertEquals(MembershipRole.OWNER, members.get(0).role());
    }

    @Test
    public void addExpenseAndGetBalances() {
        User owner = services.profiles().createLocalProfile("Praveen", "Pixel");
        Group group = services.groups().createGroup("Goa Trip", GroupType.TRIP, "INR");
        Membership friendMembership = services.groups().addLocalMember(group.groupId(), "Rahul", MembershipRole.MEMBER);
        UserId friendId = friendMembership.userId();

        Expense expense = services.expenses().addExpense(
                group.groupId(),
                "Dinner",
                Money.of("100.00"),
                owner.userId(),
                LocalDate.of(2026, 11, 18),
                SplitType.EQUAL,
                List.of(
                        SplitParticipantInput.equalShare(owner.userId()),
                        SplitParticipantInput.equalShare(friendId)));

        assertEquals(2, services.expenses().listSplits(expense.expenseId()).size());

        Map<UserId, Money> balances = toMap(services.balances().getGroupBalances(group.groupId()));
        assertEquals(Money.of("50.00"), balances.get(owner.userId()));
        assertEquals(Money.of("-50.00"), balances.get(friendId));
    }

    @Test
    public void settlementClearsBalance() {
        User owner = services.profiles().createLocalProfile("Praveen", "Pixel");
        Group group = services.groups().createGroup("Goa Trip", GroupType.TRIP, "INR");
        Membership friendMembership = services.groups().addLocalMember(group.groupId(), "Rahul", MembershipRole.MEMBER);
        UserId friendId = friendMembership.userId();

        services.expenses().addExpense(
                group.groupId(),
                "Taxi",
                Money.of("100.00"),
                owner.userId(),
                LocalDate.of(2026, 11, 18),
                SplitType.EQUAL,
                List.of(
                        SplitParticipantInput.equalShare(owner.userId()),
                        SplitParticipantInput.equalShare(friendId)));

        Settlement settlement = services.settlements().createSettlement(
                group.groupId(),
                friendId,
                owner.userId(),
                Money.of("50.00"),
                LocalDate.of(2026, 11, 19));

        Map<UserId, Money> balances = toMap(services.balances().getGroupBalances(group.groupId()));
        assertEquals(Money.zero(), balances.get(owner.userId()));
        assertEquals(Money.zero(), balances.get(friendId));
        assertEquals(1, services.settlements().listSettlements(group.groupId()).size());
        assertNotNull(settlement.settlementId());
    }

    @Test
    public void updateAndDeleteExpenseRequiresPermissionPath() {
        User owner = services.profiles().createLocalProfile("Praveen", "Pixel");
        Group group = services.groups().createGroup("Goa Trip", GroupType.TRIP, "INR");
        Membership friendMembership = services.groups().addLocalMember(group.groupId(), "Rahul", MembershipRole.MEMBER);

        Expense expense = services.expenses().addExpense(
                group.groupId(),
                "Snacks",
                Money.of("40.00"),
                owner.userId(),
                LocalDate.of(2026, 11, 18),
                SplitType.EQUAL,
                List.of(
                        SplitParticipantInput.equalShare(owner.userId()),
                        SplitParticipantInput.equalShare(friendMembership.userId())));

        Expense updated = services.expenses().updateExpense(
                expense.expenseId(),
                "Snacks Updated",
                Money.of("60.00"),
                owner.userId(),
                LocalDate.of(2026, 11, 18),
                SplitType.EQUAL,
                List.of(
                        SplitParticipantInput.equalShare(owner.userId()),
                        SplitParticipantInput.equalShare(friendMembership.userId())));

        assertEquals("Snacks Updated", updated.description());
        assertEquals(Money.of("60.00"), services.expenses().listExpenses(group.groupId()).get(0).amount());

        services.expenses().deleteExpense(expense.expenseId());
        assertEquals(0, services.expenses().listExpenses(group.groupId()).size());
    }

    @Test
    public void addExpenseRejectsNonMemberParticipant() {
        User owner = services.profiles().createLocalProfile("Praveen", "Pixel");
        Group group = services.groups().createGroup("Goa Trip", GroupType.TRIP, "INR");
        UserId stranger = UserId.generate();

        assertThrows(ApplicationException.class, () -> services.expenses().addExpense(
                group.groupId(),
                "Dinner",
                Money.of("100.00"),
                owner.userId(),
                LocalDate.of(2026, 11, 18),
                SplitType.EQUAL,
                List.of(
                        SplitParticipantInput.equalShare(owner.userId()),
                        SplitParticipantInput.equalShare(stranger))));
    }

    @Test
    public void exactSplitValidationFailureIsMapped() {
        User owner = services.profiles().createLocalProfile("Praveen", "Pixel");
        Group group = services.groups().createGroup("Goa Trip", GroupType.TRIP, "INR");
        Membership friendMembership = services.groups().addLocalMember(group.groupId(), "Rahul", MembershipRole.MEMBER);

        List<SplitParticipantInput> badExact = List.of(
                SplitParticipantInput.exact(owner.userId(), new java.math.BigDecimal("40.00")),
                SplitParticipantInput.exact(friendMembership.userId(), new java.math.BigDecimal("40.00")));

        assertThrows(ValidationException.class, () -> services.expenses().addExpense(
                group.groupId(),
                "Bad Exact",
                Money.of("100.00"),
                owner.userId(),
                LocalDate.of(2026, 11, 18),
                SplitType.EXACT,
                badExact));
    }

    private static Map<UserId, Money> toMap(List<MemberBalance> balances) {
        return balances.stream().collect(Collectors.toMap(MemberBalance::userId, MemberBalance::netAmount));
    }
}
