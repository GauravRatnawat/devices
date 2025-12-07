package com.test.devices.domain.repository;


import com.test.devices.domain.model.Device;
import com.test.devices.domain.model.DeviceState;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository {

    Device save(Device device);

    Optional<Device> findById(Long id);

    List<Device> findAll();

    List<Device> findByBrand(String brand);

    List<Device> findByState(DeviceState state);

    void delete(Device device);

}
