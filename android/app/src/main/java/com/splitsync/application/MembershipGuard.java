package com.splitsync.application;

import com.splitsync.data.repository.GroupLocalRepository;
import com.splitsync.domain.model.Membership;
import com.splitsync.domain.type.MembershipRole;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.UserId;

/**
 * Shared membership and permission checks for application services.
 */
final class MembershipGuard {

    private final GroupLocalRepository groups;

    MembershipGuard(GroupLocalRepository groups) {
        this.groups = groups;
    }

    Membership requireMembership(GroupId groupId, UserId userId) {
        Membership membership = groups.findMembership(groupId, userId);
        if (membership == null) {
            throw new ApplicationException("user is not a member of the group");
        }
        return membership;
    }

    // V1 rule: expense/settlement edit-delete allowed for creator or group OWNER.
    void requireCreatorOrOwner(GroupId groupId, UserId actorUserId, UserId createdBy) {
        if (actorUserId.equals(createdBy)) {
            return;
        }
        Membership membership = requireMembership(groupId, actorUserId);
        if (membership.role() != MembershipRole.OWNER) {
            throw new ApplicationException("only creator or group owner may modify this record");
        }
    }
}
