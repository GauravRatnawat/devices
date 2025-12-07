package com.test.devices.infrastructure.persistance;

import com.test.devices.domain.model.Device;
import com.test.devices.domain.model.DeviceState;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class JpaDeviceRepositoryTest {

    @Inject
    JpaDeviceRepository repository;

    @BeforeEach
    @Transactional
    void setUp() {
        // Clean up database before each test
        repository.findAll().forEach(device -> repository.delete(device));
    }

    @Test
    @Transactional
    void shouldSaveNewDevice() {
        // GIVEN
        Device device = Device.create("iPhone 15", "Apple", DeviceState.AVAILABLE);

        // WHEN
        Device savedDevice = repository.save(device);

        // THEN
        assertThat(savedDevice).isNotNull();
        assertThat(savedDevice.getId()).isNotNull();
        assertThat(savedDevice.getName()).isEqualTo("iPhone 15");
        assertThat(savedDevice.getBrand()).isEqualTo("Apple");
        assertThat(savedDevice.getState()).isEqualTo(DeviceState.AVAILABLE);
        assertThat(savedDevice.getCreationTime()).isNotNull();
    }

    @Test
    @Transactional
    void shouldUpdateExistingDevice() {
        // GIVEN - Save a device first
        Device device = Device.create("iPhone 15", "Apple", DeviceState.AVAILABLE);
        Device savedDevice = repository.save(device);
        Long deviceId = savedDevice.getId();

        // WHEN - Update the device
        savedDevice.updateState(DeviceState.IN_USE);
        Device updatedDevice = repository.save(savedDevice);

        // THEN
        assertThat(updatedDevice.getId()).isEqualTo(deviceId);
        assertThat(updatedDevice.getState()).isEqualTo(DeviceState.IN_USE);
    }

    @Test
    @Transactional
    void shouldFindDeviceById() {
        // GIVEN
        Device device = Device.create("Samsung Galaxy S23", "Samsung", DeviceState.AVAILABLE);
        Device savedDevice = repository.save(device);

        // WHEN
        Optional<Device> foundDevice = repository.findById(savedDevice.getId());

        // THEN
        assertThat(foundDevice).isPresent();
        assertThat(foundDevice.get().getName()).isEqualTo("Samsung Galaxy S23");
        assertThat(foundDevice.get().getBrand()).isEqualTo("Samsung");
    }

    @Test
    @Transactional
    void shouldReturnEmptyWhenDeviceNotFound() {
        // WHEN
        Optional<Device> foundDevice = repository.findById(999L);

        // THEN
        assertThat(foundDevice).isEmpty();
    }

    @Test
    @Transactional
    void shouldFindAllDevices() {
        // GIVEN
        repository.save(Device.create("iPhone 15", "Apple", DeviceState.AVAILABLE));
        repository.save(Device.create("Samsung Galaxy S23", "Samsung", DeviceState.IN_USE));
        repository.save(Device.create("Pixel 8", "Google", DeviceState.INACTIVE));

        // WHEN
        List<Device> devices = repository.findAll();

        // THEN
        assertThat(devices).hasSize(3);
        assertThat(devices).extracting(Device::getName)
                .containsExactlyInAnyOrder("iPhone 15", "Samsung Galaxy S23", "Pixel 8");
    }

    @Test
    @Transactional
    void shouldFindDevicesByBrand() {
        // GIVEN
        repository.save(Device.create("iPhone 15", "Apple", DeviceState.AVAILABLE));
        repository.save(Device.create("iPhone 14", "Apple", DeviceState.IN_USE));
        repository.save(Device.create("Samsung Galaxy S23", "Samsung", DeviceState.AVAILABLE));

        // WHEN
        List<Device> appleDevices = repository.findByBrand("Apple");

        // THEN
        assertThat(appleDevices).hasSize(2);
        assertThat(appleDevices).extracting(Device::getBrand)
                .containsOnly("Apple");
        assertThat(appleDevices).extracting(Device::getName)
                .containsExactlyInAnyOrder("iPhone 15", "iPhone 14");
    }

   
}
