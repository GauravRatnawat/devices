package com.test.devices.application.usecase;

import com.test.devices.domain.model.Device;
import com.test.devices.domain.model.DeviceState;
import com.test.devices.domain.repository.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListDevicesUseCaseTest {

    @Mock
    DeviceRepository deviceRepository;

    @InjectMocks
    ListDevicesUseCase listDevicesUseCase;

    @Test
    void shouldListAllDevicesWhenNoFiltersProvided() {
        // GIVEN - Arrange
        var device1 = Device.create("iPhone 15", "Apple", DeviceState.AVAILABLE);
        device1.setId(1L);
        var device2 = Device.create("Galaxy S24", "Samsung", DeviceState.IN_USE);
        device2.setId(2L);
        var device3 = Device.create("Pixel 8", "Google", DeviceState.INACTIVE);
        device3.setId(3L);

        when(deviceRepository.findAll()).thenReturn(Arrays.asList(device1, device2, device3));

        // WHEN - Act
        var responses = listDevicesUseCase.execute(null, null);

        // THEN - Assert
        assertThat(responses).hasSize(3);
        assertThat(responses.get(0).name()).isEqualTo("iPhone 15");
        assertThat(responses.get(1).name()).isEqualTo("Galaxy S24");
        assertThat(responses.get(2).name()).isEqualTo("Pixel 8");

        verify(deviceRepository, times(1)).findAll();
        verify(deviceRepository, never()).findByBrand(any());
        verify(deviceRepository, never()).findByState(any());
    }

    @Test
    void shouldListDevicesByBrand() {
        // GIVEN - Arrange
        var device1 = Device.create("iPhone 15", "Apple", DeviceState.AVAILABLE);
        device1.setId(1L);
        var device2 = Device.create("MacBook Pro", "Apple", DeviceState.IN_USE);
        device2.setId(2L);

        when(deviceRepository.findByBrand("Apple")).thenReturn(Arrays.asList(device1, device2));

        // WHEN - Act
        var responses = listDevicesUseCase.execute("Apple", null);

        // THEN - Assert
        assertThat(responses).hasSize(2);
        assertThat(responses).allMatch(r -> r.brand().equals("Apple"));

        verify(deviceRepository, times(1)).findByBrand("Apple");
        verify(deviceRepository, never()).findAll();
        verify(deviceRepository, never()).findByState(any());
    }

    @Test
    void shouldListDevicesByState() {
        // GIVEN - Arrange
        var device1 = Device.create("iPhone 15", "Apple", DeviceState.AVAILABLE);
        device1.setId(1L);
        var device2 = Device.create("Pixel 8", "Google", DeviceState.AVAILABLE);
        device2.setId(2L);

        when(deviceRepository.findByState(DeviceState.AVAILABLE)).thenReturn(Arrays.asList(device1, device2));

        // WHEN - Act
        var responses = listDevicesUseCase.execute(null, DeviceState.AVAILABLE);

        // THEN - Assert
        assertThat(responses).hasSize(2);
        assertThat(responses).allMatch(r -> r.state().equals(DeviceState.AVAILABLE));

        verify(deviceRepository, times(1)).findByState(DeviceState.AVAILABLE);
        verify(deviceRepository, never()).findAll();
        verify(deviceRepository, never()).findByBrand(any());
    }

    @Test
    void shouldReturnEmptyListWhenNoDevicesFound() {
        // GIVEN - Arrange
        when(deviceRepository.findAll()).thenReturn(Collections.emptyList());

        // WHEN - Act
        var responses = listDevicesUseCase.execute(null, null);

        // THEN - Assert
        assertThat(responses).isEmpty();

        verify(deviceRepository, times(1)).findAll();
    }

    @Test
    void shouldIgnoreBlankBrandFilter() {
        // GIVEN - Arrange
        var device = Device.create("iPhone 15", "Apple", DeviceState.AVAILABLE);
        device.setId(1L);

        when(deviceRepository.findAll()).thenReturn(Collections.singletonList(device));

        // WHEN - Act
        var responses = listDevicesUseCase.execute("   ", null);

        // THEN - Assert
        assertThat(responses).hasSize(1);

        verify(deviceRepository, times(1)).findAll();
        verify(deviceRepository, never()).findByBrand(any());
    }

    @Test
    void shouldPrioritizeBrandFilterOverStateFilter() {
        // GIVEN - Arrange
        var device = Device.create("iPhone 15", "Apple", DeviceState.AVAILABLE);
        device.setId(1L);

        when(deviceRepository.findByBrand("Apple")).thenReturn(Collections.singletonList(device));

        // WHEN - Act
        var responses = listDevicesUseCase.execute("Apple", DeviceState.AVAILABLE);

        // THEN - Assert
        assertThat(responses).hasSize(1);

        verify(deviceRepository, times(1)).findByBrand("Apple");
        verify(deviceRepository, never()).findByState(any());
        verify(deviceRepository, never()).findAll();
    }
}