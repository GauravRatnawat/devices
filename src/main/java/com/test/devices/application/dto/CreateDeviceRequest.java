package com.test.devices.application.dto;

import com.test.devices.domain.model.DeviceState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Request to create a new device")
public record CreateDeviceRequest(
    @Schema(description = "Name of the device", example = "iPhone 15 Pro", required = true)
    @NotBlank(message = "Name is required")
    String name,

    @Schema(description = "Brand/manufacturer of the device", example = "Apple", required = true)
    @NotBlank(message = "Brand is required")
    String brand,

    @Schema(description = "Current state of the device", example = "ACTIVE", required = true)
    @NotNull(message = "State is required")
    DeviceState state
) {}
