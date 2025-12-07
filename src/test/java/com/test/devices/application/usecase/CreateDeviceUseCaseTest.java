package com.test.devices.application.usecase;

import com.test.devices.application.dto.CreateDeviceRequest;
import com.test.devices.domain.model.Device;
import com.test.devices.domain.model.DeviceState;
import com.test.devices.domain.repository.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateDeviceUseCaseTest {

    @Mock
    DeviceRepository deviceRepository;

    @InjectMocks
    CreateDeviceUseCase createDeviceUseCase;

    @Test
    void shouldCreateDeviceForValidRequest() {
        // GIVEN - Arrange
        var request = new CreateDeviceRequest("iPhone 15", "Apple", DeviceState.AVAILABLE);
        var expectedDevice = Device.create("iPhone 15", "Apple", DeviceState.AVAILABLE);
        expectedDevice.setId(1L);

        when(deviceRepository.save(any(Device.class))).thenReturn(expectedDevice);

        // WHEN - Act
        var response = createDeviceUseCase.execute(request);

        // THEN - Assert
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("iPhone 15");
        assertThat(response.brand()).isEqualTo("Apple");
        assertThat(response.state()).isEqualTo(DeviceState.AVAILABLE);
        assertThat(response.creationTime()).isNotNull();

        verify(deviceRepository, times(1)).save(any(Device.class));
    }
}