package com.test.devices.application.usecase;

import com.test.devices.application.dto.CreateDeviceRequest;
import com.test.devices.application.dto.DeviceResponse;
import com.test.devices.domain.model.Device;
import com.test.devices.domain.repository.DeviceRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Objects;

public class CreateDeviceUseCase {

    @Inject
    DeviceRepository deviceRepository;

    @Transactional
    public DeviceResponse execute(CreateDeviceRequest request) {
        Objects.requireNonNull(request, "Request cannot be null");

        Device device = Device.create(request.name(), request.brand(), request.state());
        Device savedDevice = deviceRepository.save(device);

        return DeviceResponse.from(savedDevice);
    }
}
