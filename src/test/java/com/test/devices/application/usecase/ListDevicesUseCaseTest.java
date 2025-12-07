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

}