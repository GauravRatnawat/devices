package com.test.devices.application.dto;


import com.test.devices.domain.model.Device;
import com.test.devices.domain.model.DeviceState;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Device information response")
public record DeviceResponse(
    @Schema(description = "Unique identifier of the device", example = "1")
    Long id,

    @Schema(description = "Name of the device", example = "iPhone 15 Pro")
    String name,

    @Schema(description = "Brand/manufacturer of the device", example = "Apple")
    String brand,

    @Schema(description = "Current state of the device", example = "ACTIVE")
    DeviceState state,

    @Schema(description = "Timestamp when the device was created", example = "2025-12-07T10:30:00")
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
