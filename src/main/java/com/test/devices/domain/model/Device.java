package com.test.devices.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Device {

    private Long id;
    private String name;
    private String brand;
    private DeviceState state;
    private LocalDateTime creationTime;

    private Device(Long id, String name, String brand, DeviceState state, LocalDateTime creationTime) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.state = state;
        this.creationTime = creationTime;
    }

    public static Device create(String name, String brand, DeviceState state) {
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(brand, "Brand cannot be null");
        Objects.requireNonNull(state, "State cannot be null");

        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (brand.isBlank()) {
            throw new IllegalArgumentException("Brand cannot be blank");
        }

        return new Device(null, name, brand, state, LocalDateTime.now());
    }

    public void updateState(DeviceState newState) {
        Objects.requireNonNull(newState, "State cannot be null");
        this.state = newState;
    }

    public void updateDetails(String newName, String newBrand) {
        if (this.state == DeviceState.IN_USE) {
            throw new IllegalStateException("Cannot update name or brand for device in use");
        }

        if (newName != null) {
            if (newName.isBlank()) {
                throw new IllegalArgumentException("Name cannot be blank");
            }
            this.name = newName;
        }

        if (newBrand != null) {
            if (newBrand.isBlank()) {
                throw new IllegalArgumentException("Brand cannot be blank");
            }
            this.brand = newBrand;
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public DeviceState getState() {
        return state;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }
}
