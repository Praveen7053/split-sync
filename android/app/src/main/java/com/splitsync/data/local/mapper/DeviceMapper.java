package com.splitsync.data.local.mapper;

import com.splitsync.data.local.entity.DeviceEntity;
import com.splitsync.domain.model.Device;
import com.splitsync.domain.valueobject.DeviceId;
import com.splitsync.domain.valueobject.UserId;

public final class DeviceMapper {

    private DeviceMapper() {
    }

    public static DeviceEntity toEntity(Device device) {
        DeviceEntity entity = new DeviceEntity();
        entity.deviceId = device.deviceId().value();
        entity.userId = device.userId().value();
        entity.deviceName = device.deviceName();
        entity.createdAt = device.createdAt();
        entity.updatedAt = device.updatedAt();
        return entity;
    }

    public static Device toDomain(DeviceEntity entity) {
        return Device.restore(
                DeviceId.of(entity.deviceId),
                UserId.of(entity.userId),
                entity.deviceName,
                entity.createdAt,
                entity.updatedAt);
    }
}
