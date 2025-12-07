package com.test.devices.application.usecase;

import com.test.devices.domain.model.Device;
import com.test.devices.domain.model.DeviceState;
import com.test.devices.domain.repository.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class DeleteDeviceUseCaseTest {

    @Mock
    DeviceRepository deviceRepository;

    @InjectMocks
    DeleteDeviceUseCase deleteDeviceUseCase;

    @Test
    void shouldDeleteDeviceSuccessfully() {
        // GIVEN - An available device
        Device device = Device.create("iPhone 15", "Apple", DeviceState.AVAILABLE);
        device.setId(1L);

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
        doNothing().when(deviceRepository).delete(device);

        // WHEN - Deleting device
        deleteDeviceUseCase.execute(1L);

        // THEN - Device is deleted
        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceRepository, times(1)).delete(device);
    }

    @Test
    void shouldThrowExceptionWhenDeviceNotFound() {
        // GIVEN - Non-existent device
        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        // WHEN/THEN - Exception is thrown
        assertThatThrownBy(() -> deleteDeviceUseCase.execute(1L))
                .isInstanceOf(DeviceNotFoundException.class);

        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceRepository, never()).delete(any(Device.class));
    }
}