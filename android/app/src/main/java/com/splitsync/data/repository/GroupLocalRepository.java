package com.splitsync.data.repository;

import androidx.room.RoomDatabase;

import com.splitsync.data.local.dao.GroupDao;
import com.splitsync.data.local.dao.MembershipDao;
import com.splitsync.data.local.entity.GroupEntity;
import com.splitsync.data.local.entity.MembershipEntity;
import com.splitsync.data.local.mapper.GroupMapper;
import com.splitsync.data.local.mapper.MembershipMapper;
import com.splitsync.domain.model.Group;
import com.splitsync.domain.model.Membership;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.UserId;

import java.util.ArrayList;
import java.util.List;

/**
 * Room-backed group and membership persistence.
 */
public class GroupLocalRepository {

    private final RoomDatabase database;
    private final GroupDao groupDao;
    private final MembershipDao membershipDao;

    public GroupLocalRepository(RoomDatabase database, GroupDao groupDao, MembershipDao membershipDao) {
        this.database = database;
        this.groupDao = groupDao;
        this.membershipDao = membershipDao;
    }

    // Persist group and owner membership together so a group is never owner-less locally.
    public void saveGroupWithOwner(Group group, Membership ownerMembership) {
        database.runInTransaction(() -> {
            groupDao.insert(GroupMapper.toEntity(group));
            membershipDao.insert(MembershipMapper.toEntity(ownerMembership));
        });
    }

    public void saveMembership(Membership membership) {
        membershipDao.insert(MembershipMapper.toEntity(membership));
    }

    public Group findById(GroupId groupId) {
        GroupEntity entity = groupDao.findById(groupId.value());
        return entity == null ? null : GroupMapper.toDomain(entity);
    }

    public List<Group> findAll() {
        List<Group> groups = new ArrayList<>();
        for (GroupEntity entity : groupDao.findAll()) {
            groups.add(GroupMapper.toDomain(entity));
        }
        return groups;
    }

    public List<Membership> findMemberships(GroupId groupId) {
        List<Membership> memberships = new ArrayList<>();
        for (MembershipEntity entity : membershipDao.findByGroupId(groupId.value())) {
            memberships.add(MembershipMapper.toDomain(entity));
        }
        return memberships;
    }

    public Membership findMembership(GroupId groupId, UserId userId) {
        MembershipEntity entity = membershipDao.findByGroupAndUser(groupId.value(), userId.value());
        return entity == null ? null : MembershipMapper.toDomain(entity);
    }
}
