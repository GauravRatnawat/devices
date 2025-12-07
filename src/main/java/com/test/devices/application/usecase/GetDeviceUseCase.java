package com.test.devices.application.usecase;

import com.test.devices.application.dto.DeviceResponse;
import com.test.devices.application.exception.DeviceNotFoundException;
import com.test.devices.domain.model.Device;
import com.test.devices.domain.repository.DeviceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Objects;

@ApplicationScoped
public class GetDeviceUseCase {

    @Inject
    DeviceRepository deviceRepository;

    @Transactional
    public DeviceResponse execute(Long id) {
        Objects.requireNonNull(id, "Device ID cannot be null");

        Device device = deviceRepository.findById(id)
            .orElseThrow(() -> new DeviceNotFoundException(id));

        return DeviceResponse.from(device);
    }
}
