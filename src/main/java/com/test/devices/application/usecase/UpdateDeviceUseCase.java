package com.test.devices.application.usecase;

import com.test.devices.application.dto.DeviceResponse;
import com.test.devices.application.dto.UpdateDeviceRequest;
import com.test.devices.application.exception.DeviceNotFoundException;
import com.test.devices.application.exception.DeviceValidationException;
import com.test.devices.domain.model.Device;
import com.test.devices.domain.repository.DeviceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Objects;

@ApplicationScoped
public class UpdateDeviceUseCase {

    @Inject
    DeviceRepository deviceRepository;

    @Transactional
    public DeviceResponse execute(Long id, UpdateDeviceRequest request) {
        Objects.requireNonNull(id, "Device ID cannot be null");
        Objects.requireNonNull(request, "Request cannot be null");

        Device device = deviceRepository.findById(id)
            .orElseThrow(() -> new DeviceNotFoundException(id));

        try {
            if (request.name() != null || request.brand() != null) {
                device.updateDetails(request.name(), request.brand());
            }

            if (request.state() != null) {
                device.updateState(request.state());
            }

            Device updatedDevice = deviceRepository.save(device);

            return DeviceResponse.from(updatedDevice);

        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new DeviceValidationException(e.getMessage(), e);
        }
    }
}
