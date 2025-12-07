package com.test.devices.application.dto;

import com.test.devices.domain.model.DeviceState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateDeviceRequest(
    @NotBlank(message = "Name is required")
    String name,

    @NotBlank(message = "Brand is required")
    String brand,

    @NotNull(message = "State is required")
    DeviceState state
) {}
