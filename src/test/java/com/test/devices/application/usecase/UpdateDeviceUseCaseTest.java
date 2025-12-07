package com.test.devices.application.usecase;

import com.test.devices.application.dto.DeviceResponse;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UpdateDeviceUseCaseTest {

    @Mock
    DeviceRepository deviceRepository;

    @InjectMocks
    UpdateDeviceUseCase updateDeviceUseCase;

    @Test
    void shouldUpdateDeviceStateSuccessfully() {
        Device device = Device.create("iPhone 15", "Apple", DeviceState.AVAILABLE);
        device.setId(1L);

        UpdateDeviceRequest request = new UpdateDeviceRequest(null, null, DeviceState.IN_USE);

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
        when(deviceRepository.save(any(Device.class))).thenReturn(device);

        DeviceResponse response = updateDeviceUseCase.execute(1L, request);

        assertThat(response).isNotNull();
        assertThat(response.state()).isEqualTo(DeviceState.IN_USE);

        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceRepository, times(1)).save(any(Device.class));
    }

}