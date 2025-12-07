package com.test.devices.application.dto;


import com.test.devices.domain.model.DeviceState;

public record UpdateDeviceRequest(
    String name,
    String brand,
    DeviceState state
) {}
