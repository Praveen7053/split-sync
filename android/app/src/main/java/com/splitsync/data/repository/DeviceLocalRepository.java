package com.splitsync.data.repository;

import com.splitsync.data.local.dao.DeviceDao;
import com.splitsync.data.local.entity.DeviceEntity;
import com.splitsync.data.local.mapper.DeviceMapper;
import com.splitsync.domain.model.Device;
import com.splitsync.domain.valueobject.DeviceId;
import com.splitsync.domain.valueobject.UserId;

/**
 * Room-backed device identity persistence.
 */
public class DeviceLocalRepository {

    private final DeviceDao deviceDao;

    public DeviceLocalRepository(DeviceDao deviceDao) {
        this.deviceDao = deviceDao;
    }

    public void save(Device device) {
        deviceDao.insert(DeviceMapper.toEntity(device));
    }

    public Device findById(DeviceId deviceId) {
        DeviceEntity entity = deviceDao.findById(deviceId.value());
        return entity == null ? null : DeviceMapper.toDomain(entity);
    }

    public Device findByUserId(UserId userId) {
        DeviceEntity entity = deviceDao.findByUserId(userId.value());
        return entity == null ? null : DeviceMapper.toDomain(entity);
    }
}
