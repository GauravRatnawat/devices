package com.test.devices.application.usecase;

import com.test.devices.application.exception.DeviceNotFoundException;
import com.test.devices.domain.model.Device;
import com.test.devices.domain.model.DeviceState;
import com.test.devices.domain.repository.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetDeviceUseCaseTest {

    @Mock
    DeviceRepository deviceRepository;

    @InjectMocks
    GetDeviceUseCase getDeviceUseCase;

    @Test
    void shouldGetDeviceByIdSuccessfully() {
        // GIVEN - Arrange
        var device = Device.create("iPhone 15", "Apple", DeviceState.AVAILABLE);
        device.setId(1L);

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        // WHEN - Act
        var response = getDeviceUseCase.execute(1L);

        // THEN - Assert
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("iPhone 15");
        assertThat(response.brand()).isEqualTo("Apple");
        assertThat(response.state()).isEqualTo(DeviceState.AVAILABLE);

        verify(deviceRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeviceNotFound() {
        // GIVEN - Arrange
        when(deviceRepository.findById(999L)).thenReturn(Optional.empty());

        // WHEN & THEN - Act & Assert
        assertThatThrownBy(() -> getDeviceUseCase.execute(999L))
                .isInstanceOf(DeviceNotFoundException.class)
                .hasMessageContaining("Device not found");

        verify(deviceRepository, times(1)).findById(999L);
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        // GIVEN - Arrange
        Long id = null;

        // WHEN & THEN - Act & Assert
        assertThatThrownBy(() -> getDeviceUseCase.execute(id))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Device ID cannot be null");

        verify(deviceRepository, never()).findById(any());
    }

    @Test
    void shouldGetDeviceWithDifferentStates() {
        // GIVEN - Arrange
        var inUseDevice = Device.create("Galaxy S24", "Samsung", DeviceState.IN_USE);
        inUseDevice.setId(2L);

        var inactiveDevice = Device.create("Pixel 8", "Google", DeviceState.INACTIVE);
        inactiveDevice.setId(3L);

        when(deviceRepository.findById(2L)).thenReturn(Optional.of(inUseDevice));
        when(deviceRepository.findById(3L)).thenReturn(Optional.of(inactiveDevice));

        // WHEN - Act
        var response1 = getDeviceUseCase.execute(2L);
        var response2 = getDeviceUseCase.execute(3L);

        // THEN - Assert
        assertThat(response1.state()).isEqualTo(DeviceState.IN_USE);
        assertThat(response2.state()).isEqualTo(DeviceState.INACTIVE);

        verify(deviceRepository, times(1)).findById(2L);
        verify(deviceRepository, times(1)).findById(3L);
    }

}