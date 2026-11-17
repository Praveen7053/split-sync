package com.splitsync.application;

import com.splitsync.data.repository.ExpenseLocalRepository;
import com.splitsync.data.repository.GroupLocalRepository;
import com.splitsync.domain.model.Expense;
import com.splitsync.domain.model.ExpenseSplit;
import com.splitsync.domain.model.Group;
import com.splitsync.domain.model.User;
import com.splitsync.domain.type.SplitType;
import com.splitsync.domain.validation.SplitCalculator;
import com.splitsync.domain.validation.SplitParticipantInput;
import com.splitsync.domain.valueobject.ExpenseId;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.Money;
import com.splitsync.domain.valueobject.UserId;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Expense create/update/delete use cases with transactional split persistence.
 */
public class ExpenseService {

    private final ProfileService profiles;
    private final GroupLocalRepository groups;
    private final ExpenseLocalRepository expenses;
    private final MembershipGuard membershipGuard;

    public ExpenseService(ProfileService profiles, GroupLocalRepository groups, ExpenseLocalRepository expenses) {
        this.profiles = profiles;
        this.groups = groups;
        this.expenses = expenses;
        this.membershipGuard = new MembershipGuard(groups);
    }

    public Expense addExpense(
            GroupId groupId,
            String description,
            Money amount,
            UserId paidBy,
            LocalDate expenseDate,
            SplitType splitType,
            List<SplitParticipantInput> participants) {
        User actor = profiles.getLocalProfile();
        Group group = requireGroup(groupId);
        membershipGuard.requireMembership(groupId, actor.userId());
        membershipGuard.requireMembership(groupId, paidBy);
        requireParticipantsAreMembers(groupId, participants);

        // Group currency is the only allowed currency for expenses in V1.
        Expense expense = Expense.create(groupId, description, amount, group.currency(), paidBy, actor.userId(), expenseDate);
        List<ExpenseSplit> splits = SplitCalculator.calculate(expense.expenseId(), expense.amount(), splitType, participants);
        expenses.saveExpenseWithSplits(expense, splits);
        return expense;
    }

    public Expense updateExpense(
            ExpenseId expenseId,
            String description,
            Money amount,
            UserId paidBy,
            LocalDate expenseDate,
            SplitType splitType,
            List<SplitParticipantInput> participants) {
        User actor = profiles.getLocalProfile();
        Expense existing = requireExpense(expenseId);
        membershipGuard.requireCreatorOrOwner(existing.groupId(), actor.userId(), existing.createdBy());
        membershipGuard.requireMembership(existing.groupId(), paidBy);
        requireParticipantsAreMembers(existing.groupId(), participants);

        Group group = requireGroup(existing.groupId());
        Expense updated = Expense.restore(
                existing.expenseId(),
                existing.groupId(),
                description,
                amount,
                group.currency(),
                paidBy,
                existing.createdBy(),
                expenseDate,
                existing.category(),
                existing.notes(),
                existing.createdAt(),
                Instant.now());

        // Recreate splits under the same expense id so balances stay consistent.
        List<ExpenseSplit> splits = SplitCalculator.calculate(updated.expenseId(), updated.amount(), splitType, participants);
        expenses.replaceExpenseWithSplits(updated, splits);
        return updated;
    }

    public void deleteExpense(ExpenseId expenseId) {
        User actor = profiles.getLocalProfile();
        Expense existing = requireExpense(expenseId);
        membershipGuard.requireCreatorOrOwner(existing.groupId(), actor.userId(), existing.createdBy());
        expenses.deleteExpense(expenseId);
    }

    public List<Expense> listExpenses(GroupId groupId) {
        requireGroup(groupId);
        return expenses.findByGroupId(groupId);
    }

    public Expense getExpense(ExpenseId expenseId) {
        return requireExpense(expenseId);
    }

    public List<ExpenseSplit> listSplits(ExpenseId expenseId) {
        requireExpense(expenseId);
        return expenses.findSplits(expenseId);
    }

    private Group requireGroup(GroupId groupId) {
        Group group = groups.findById(groupId);
        if (group == null) {
            throw new ApplicationException("group not found");
        }
        return group;
    }

    private Expense requireExpense(ExpenseId expenseId) {
        Expense expense = expenses.findById(expenseId);
        if (expense == null) {
            throw new ApplicationException("expense not found");
        }
        return expense;
    }

    private void requireParticipantsAreMembers(GroupId groupId, List<SplitParticipantInput> participants) {
        if (participants == null || participants.isEmpty()) {
            throw new ApplicationException("at least one split participant is required");
        }
        Set<UserId> seen = new HashSet<>();
        for (SplitParticipantInput participant : participants) {
            if (!seen.add(participant.userId())) {
                throw new ApplicationException("duplicate split participant");
            }
            membershipGuard.requireMembership(groupId, participant.userId());
        }
    }
}
