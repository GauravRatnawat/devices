package com.test.devices.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class DeviceTest {

    @Test
    void shouldCreateDeviceWithValidParameters() {
        // GIVEN - Valid device parameters
        String name = "iPhone 15";
        String brand = "Apple";
        DeviceState state = DeviceState.AVAILABLE;

        // WHEN - Creating a device
        Device device = Device.create(name, brand, state);

        // THEN - Device is created with correct values
        assertThat(device).isNotNull();
        assertThat(device.getName()).isEqualTo(name);
        assertThat(device.getBrand()).isEqualTo(brand);
        assertThat(device.getState()).isEqualTo(state);
        assertThat(device.getCreationTime()).isNotNull();
        assertThat(device.getCreationTime()).isBefore(LocalDateTime.now().plusSeconds(1));
    }
}