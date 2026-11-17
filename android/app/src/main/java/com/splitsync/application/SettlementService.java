package com.splitsync.application;

import com.splitsync.data.repository.GroupLocalRepository;
import com.splitsync.data.repository.SettlementLocalRepository;
import com.splitsync.domain.model.Group;
import com.splitsync.domain.model.Settlement;
import com.splitsync.domain.model.User;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.Money;
import com.splitsync.domain.valueobject.SettlementId;
import com.splitsync.domain.valueobject.UserId;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Settlement create/update/delete use cases.
 */
public class SettlementService {

    private final ProfileService profiles;
    private final GroupLocalRepository groups;
    private final SettlementLocalRepository settlements;
    private final MembershipGuard membershipGuard;

    public SettlementService(ProfileService profiles, GroupLocalRepository groups, SettlementLocalRepository settlements) {
        this.profiles = profiles;
        this.groups = groups;
        this.settlements = settlements;
        this.membershipGuard = new MembershipGuard(groups);
    }

    public Settlement createSettlement(GroupId groupId, UserId paidBy, UserId paidTo, Money amount, LocalDate settlementDate) {
        User actor = profiles.getLocalProfile();
        Group group = requireGroup(groupId);
        membershipGuard.requireMembership(groupId, actor.userId());
        membershipGuard.requireMembership(groupId, paidBy);
        membershipGuard.requireMembership(groupId, paidTo);

        Settlement settlement = Settlement.create(groupId, paidBy, paidTo, amount, group.currency(), actor.userId(), settlementDate);
        settlements.save(settlement);
        return settlement;
    }

    public Settlement updateSettlement(SettlementId settlementId, UserId paidBy, UserId paidTo, Money amount, LocalDate settlementDate) {
        User actor = profiles.getLocalProfile();
        Settlement existing = requireSettlement(settlementId);
        membershipGuard.requireCreatorOrOwner(existing.groupId(), actor.userId(), existing.createdBy());
        membershipGuard.requireMembership(existing.groupId(), paidBy);
        membershipGuard.requireMembership(existing.groupId(), paidTo);

        Group group = requireGroup(existing.groupId());
        Settlement updated = Settlement.restore(
                existing.settlementId(),
                existing.groupId(),
                paidBy,
                paidTo,
                amount,
                group.currency(),
                existing.createdBy(),
                settlementDate,
                existing.notes(),
                existing.createdAt(),
                Instant.now());
        settlements.update(updated);
        return updated;
    }

    public void deleteSettlement(SettlementId settlementId) {
        User actor = profiles.getLocalProfile();
        Settlement existing = requireSettlement(settlementId);
        membershipGuard.requireCreatorOrOwner(existing.groupId(), actor.userId(), existing.createdBy());
        settlements.delete(settlementId);
    }

    public List<Settlement> listSettlements(GroupId groupId) {
        requireGroup(groupId);
        return settlements.findByGroupId(groupId);
    }

    private Group requireGroup(GroupId groupId) {
        Group group = groups.findById(groupId);
        if (group == null) {
            throw new ApplicationException("group not found");
        }
        return group;
    }

    private Settlement requireSettlement(SettlementId settlementId) {
        Settlement settlement = settlements.findById(settlementId);
        if (settlement == null) {
            throw new ApplicationException("settlement not found");
        }
        return settlement;
    }
}
