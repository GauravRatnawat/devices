package com.test.devices.infrastructure.persistance;

import com.test.devices.domain.model.Device;
import com.test.devices.domain.model.DeviceState;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
}
