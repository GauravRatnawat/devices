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

    @Test
    void shouldThrowExceptionWhenCreatingDeviceWithNullName() {
        // GIVEN - Null name
        String name = null;

        // WHEN/THEN - Creating device throws exception
        assertThatThrownBy(() -> Device.create(name, "Apple", DeviceState.AVAILABLE))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Name cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenCreatingDeviceWithBlankName() {
        // GIVEN - Blank name
        String name = "   ";

        // WHEN/THEN - Creating device throws exception
        assertThatThrownBy(() -> Device.create(name, "Apple", DeviceState.AVAILABLE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Name cannot be blank");
    }

    @Test
    void shouldThrowExceptionWhenCreatingDeviceWithNullBrand() {
        // GIVEN - Null brand
        String brand = null;

        // WHEN/THEN - Creating device throws exception
        assertThatThrownBy(() -> Device.create("iPhone", brand, DeviceState.AVAILABLE))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Brand cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenCreatingDeviceWithBlankBrand() {
        // GIVEN - Blank name
        String brand = "   ";

        // WHEN/THEN - Creating device throws exception
        assertThatThrownBy(() -> Device.create("iPhone", brand, DeviceState.AVAILABLE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Brand cannot be blank");
    }

    @Test
    void shouldThrowExceptionWhenCreatingDeviceWithNullState() {
        // GIVEN - Null state

        // WHEN/THEN - Creating device throws exception
        assertThatThrownBy(() -> Device.create("iPhone", "Apple", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("State cannot be null");
    }

    @Test
    void shouldUpdateDeviceState() {
        // GIVEN - A device
        Device device = Device.create("iPhone 15", "Apple", DeviceState.AVAILABLE);

        // WHEN - Updating state
        device.updateState(DeviceState.IN_USE);

        // THEN - State is updated
        assertThat(device.getState()).isEqualTo(DeviceState.IN_USE);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingDeviceWithNullState() {
        // GIVEN - A device
        Device device = Device.create("iPhone 15", "Apple", DeviceState.AVAILABLE);

        // WHEN
        // THEN
        assertThatThrownBy(() -> device.updateState(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("State cannot be null");
    }

    @Test
    void shouldUpdateDeviceDetailsWhenNotInUse() {
        // GIVEN - A device not in use
        Device device = Device.create("iPhone 15", "Apple", DeviceState.AVAILABLE);

        // WHEN - Updating details
        device.updateDetails("iPhone 15 Pro", "Apple Inc");

        // THEN - Details are updated
        assertThat(device.getName()).isEqualTo("iPhone 15 Pro");
        assertThat(device.getBrand()).isEqualTo("Apple Inc");
    }

    @Test
    void shouldNotUpdateDeviceDetailsWhenInUse() {
        // GIVEN - A device in use
        Device device = Device.create("iPhone 15", "Apple", DeviceState.IN_USE);

        // WHEN/THEN - Updating details throws exception
        assertThatThrownBy(() -> device.updateDetails("iPhone 15 Pro", "Apple Inc"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot update name or brand for device in use");
    }
}