package com.test.devices.domain.repository;


import com.test.devices.domain.model.Device;

import java.util.Optional;

public interface DeviceRepository {

    Device save(Device device);

    Optional<Device> findById(Long id);

    void delete(Device device);

}

