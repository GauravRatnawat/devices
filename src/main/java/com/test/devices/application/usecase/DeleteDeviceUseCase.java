package com.test.devices.application.usecase;

import com.test.devices.application.exception.DeviceNotFoundException;
import com.test.devices.application.exception.DeviceValidationException;
import com.test.devices.domain.model.Device;
import com.test.devices.domain.repository.DeviceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Objects;

@ApplicationScoped
public class DeleteDeviceUseCase {

    @Inject
    DeviceRepository deviceRepository;

    @Transactional
    public void execute(Long id) {
        Objects.requireNonNull(id, "Device ID cannot be null");

        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));

        if (!device.canBeDeleted()) {
            throw new DeviceValidationException("Cannot delete device that is in use");
        }

        deviceRepository.delete(device);
    }
}
