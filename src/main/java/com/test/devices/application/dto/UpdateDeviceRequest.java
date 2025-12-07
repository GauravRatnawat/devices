package com.test.devices.application.dto;


import com.test.devices.domain.model.DeviceState;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Request to update an existing device")
public record UpdateDeviceRequest(
    @Schema(description = "Name of the device", example = "iPhone 15 Pro Max")
    String name,

    @Schema(description = "Brand/manufacturer of the device", example = "Apple")
    String brand,

    @Schema(description = "Current state of the device", example = "MAINTENANCE")
    DeviceState state
) {}
