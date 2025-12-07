package com.test.devices.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Device {

    private Long id;
    private String name;
    private String brand;
    private DeviceState state;
    private final LocalDateTime creationTime;

    private Device(Long id, String name, String brand, DeviceState state, LocalDateTime creationTime) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.state = state;
        this.creationTime = creationTime;
    }

    public static Device create(String name, String brand, DeviceState state) {
        validateName(name);
        validateBrand(brand);
        validateState(state);

        return new Device(null, name, brand, state, LocalDateTime.now());
    }

    public static Device reconstitute(Long id, String name, String brand, DeviceState state, LocalDateTime creationTime) {
        return new Device(id, name, brand, state, creationTime);
    }

    public void updateState(DeviceState newState) {
        validateState(newState);
        this.state = newState;
    }

    public void updateDetails(String newName, String newBrand) {
        validateNotInUse();

        if (newName != null) {
            validateName(newName);
            this.name = newName;
        }

        if (newBrand != null) {
            validateBrand(newBrand);
            this.brand = newBrand;
        }
    }

    public boolean canBeDeleted() {
        return this.state != DeviceState.IN_USE;
    }

    public boolean isInUse() {
        return this.state == DeviceState.IN_USE;
    }

    // Getters
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

    // Private validation methods
    private static void validateName(String name) {
        Objects.requireNonNull(name, "Name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
    }

    private static void validateBrand(String brand) {
        Objects.requireNonNull(brand, "Brand cannot be null");
        if (brand.isBlank()) {
            throw new IllegalArgumentException("Brand cannot be blank");
        }
    }

    private static void validateState(DeviceState state) {
        Objects.requireNonNull(state, "State cannot be null");
    }

    private void validateNotInUse() {
        if (isInUse()) {
            throw new IllegalStateException("Cannot update name or brand for device in use");
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(id, device.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", state=" + state +
                ", creationTime=" + creationTime +
                '}';
    }
}
