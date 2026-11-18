package com.splitsync.application;

import com.splitsync.data.repository.GroupLocalRepository;
import com.splitsync.data.repository.UserLocalRepository;
import com.splitsync.domain.model.Group;
import com.splitsync.domain.model.Membership;
import com.splitsync.domain.model.User;
import com.splitsync.domain.type.GroupType;
import com.splitsync.domain.type.MembershipRole;
import com.splitsync.domain.valueobject.CurrencyCode;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.UserId;

import java.util.List;

/**
 * Group and membership use cases for offline V1a.
 */
public class GroupService {

    private final ProfileService profiles;
    private final UserLocalRepository users;
    private final GroupLocalRepository groups;
    private final MembershipGuard membershipGuard;

    public GroupService(ProfileService profiles, UserLocalRepository users, GroupLocalRepository groups) {
        this.profiles = profiles;
        this.users = users;
        this.groups = groups;
        this.membershipGuard = new MembershipGuard(groups);
    }

    public Group createGroup(String name, GroupType groupType, String currencyCode) {
        User creator = profiles.getLocalProfile();
        Group group = Group.create(name, groupType, CurrencyCode.of(currencyCode), creator.userId());
        Membership owner = Membership.createOwner(group.groupId(), creator.userId());
        groups.saveGroupWithOwner(group, owner);
        return group;
    }

    /**
     * Creates a local member profile and adds them to the group (offline-only flow).
     */
    public Membership addLocalMember(GroupId groupId, String displayName, MembershipRole role) {
        return addLocalMember(groupId, displayName, null, role);
    }

    public Membership addLocalMember(GroupId groupId, String displayName, String phoneNumber, MembershipRole role) {
        User actor = profiles.getLocalProfile();
        membershipGuard.requireMembership(groupId, actor.userId());

        if (groups.findById(groupId) == null) {
            throw new ApplicationException("group not found");
        }

        MembershipRole effectiveRole = role == null ? MembershipRole.MEMBER : role;
        if (effectiveRole == MembershipRole.OWNER) {
            throw new ApplicationException("cannot add another OWNER via addLocalMember");
        }

        User memberUser = User.create(displayName);
        if (phoneNumber != null && !phoneNumber.isBlank()) {
            memberUser = memberUser.withProfile(displayName, phoneNumber, null, null);
        }
        users.save(memberUser);
        Membership membership = Membership.create(groupId, memberUser.userId(), effectiveRole);
        groups.saveMembership(membership);
        return membership;
    }

    public Membership addExistingMember(GroupId groupId, UserId userId, MembershipRole role) {
        User actor = profiles.getLocalProfile();
        membershipGuard.requireMembership(groupId, actor.userId());

        if (groups.findById(groupId) == null) {
            throw new ApplicationException("group not found");
        }
        if (users.findById(userId) == null) {
            throw new ApplicationException("user not found");
        }
        if (groups.findMembership(groupId, userId) != null) {
            throw new ApplicationException("user is already a group member");
        }

        MembershipRole effectiveRole = role == null ? MembershipRole.MEMBER : role;
        if (effectiveRole == MembershipRole.OWNER) {
            throw new ApplicationException("cannot add another OWNER via addExistingMember");
        }

        Membership membership = Membership.create(groupId, userId, effectiveRole);
        groups.saveMembership(membership);
        return membership;
    }

    public List<Group> listGroups() {
        return groups.findAll();
    }

    public Group getGroup(GroupId groupId) {
        Group group = groups.findById(groupId);
        if (group == null) {
            throw new ApplicationException("group not found");
        }
        return group;
    }

    public List<Membership> listMembers(GroupId groupId) {
        getGroup(groupId);
        return groups.findMemberships(groupId);
    }
}
