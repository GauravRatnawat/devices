package com.test.devices.application.dto;


import com.test.devices.domain.model.Device;
import com.test.devices.domain.model.DeviceState;

import java.time.LocalDateTime;

public record DeviceResponse(
    Long id,
    String name,
    String brand,
    DeviceState state,
    LocalDateTime creationTime
) {
    public static DeviceResponse from(Device device) {
        return new DeviceResponse(
            device.getId(),
            device.getName(),
            device.getBrand(),
            device.getState(),
            device.getCreationTime()
        );
    }
}
