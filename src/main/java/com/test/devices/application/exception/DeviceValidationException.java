package com.test.devices.application.exception;

public class DeviceValidationException extends RuntimeException {
    public DeviceValidationException(String message) {
        super(message);
    }

    public DeviceValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
