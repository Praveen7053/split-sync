package com.splitsync.application;

import com.splitsync.data.repository.DeviceLocalRepository;
import com.splitsync.data.repository.UserLocalRepository;
import com.splitsync.domain.model.Device;
import com.splitsync.domain.model.User;
import com.splitsync.domain.validation.DomainValidator;

/**
 * Local profile and device identity use cases.
 */
public class ProfileService {

    private final UserLocalRepository users;
    private final DeviceLocalRepository devices;

    public ProfileService(UserLocalRepository users, DeviceLocalRepository devices) {
        this.users = users;
        this.devices = devices;
    }

    public User createLocalProfile(String displayName, String deviceName) {
        if (users.findLocalProfile() != null) {
            throw new ApplicationException("local profile already exists");
        }
        User user = User.create(displayName);
        users.save(user);
        ensureDevice(user, deviceName);
        return user;
    }

    public User updateLocalProfile(String displayName, String phoneNumber, String email, String profilePhotoPath) {
        User existing = requireLocalProfile();
        User updated = existing.withProfile(displayName, phoneNumber, email, profilePhotoPath);
        users.update(updated);
        return updated;
    }

    public User getLocalProfile() {
        return requireLocalProfile();
    }

    public User findLocalProfileOrNull() {
        return users.findLocalProfile();
    }

    public Device ensureDevice(User user, String deviceName) {
        DomainValidator.requireNonNull(user, "user");
        Device existing = devices.findByUserId(user.userId());
        if (existing != null) {
            return existing;
        }
        Device device = Device.create(user.userId(), deviceName);
        devices.save(device);
        return device;
    }

    private User requireLocalProfile() {
        User profile = users.findLocalProfile();
        if (profile == null) {
            throw new ApplicationException("local profile not found");
        }
        return profile;
    }
}
