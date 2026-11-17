package com.splitsync.data.repository;

import com.splitsync.data.local.dao.UserDao;
import com.splitsync.data.local.entity.UserEntity;
import com.splitsync.data.local.mapper.UserMapper;
import com.splitsync.domain.model.User;
import com.splitsync.domain.valueobject.UserId;

/**
 * Room-backed user persistence for offline profile storage.
 */
public class UserLocalRepository {

    private final UserDao userDao;

    public UserLocalRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    public void save(User user) {
        userDao.insert(UserMapper.toEntity(user));
    }

    public void update(User user) {
        userDao.update(UserMapper.toEntity(user));
    }

    public User findById(UserId userId) {
        UserEntity entity = userDao.findById(userId.value());
        return entity == null ? null : UserMapper.toDomain(entity);
    }

    public User findLocalProfile() {
        UserEntity entity = userDao.findFirst();
        return entity == null ? null : UserMapper.toDomain(entity);
    }
}
