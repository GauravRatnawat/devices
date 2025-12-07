package com.test.devices.application.usecase;

import com.test.devices.application.dto.DeviceResponse;
import com.test.devices.domain.model.Device;
import com.test.devices.domain.model.DeviceState;
import com.test.devices.domain.repository.DeviceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ListDevicesUseCase {


    @Inject
    DeviceRepository deviceRepository;

    @Transactional
    public List<DeviceResponse> execute(String brand, DeviceState state) {

        List<Device> devices;

        if (brand != null && !brand.isBlank()) {
            devices = deviceRepository.findByBrand(brand);
        } else if (state != null) {
            devices = deviceRepository.findByState(state);
        } else {
            devices = deviceRepository.findAll();
        }

        return devices.stream()
                .map(DeviceResponse::from)
                .collect(Collectors.toList());
    }
}
