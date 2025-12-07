package com.test.devices.presentation.exception;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Error response returned when an API operation fails")
public class ErrorResponse {
    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "Error message describing what went wrong", example = "Device not found with id: 123")
    private String message;

    public ErrorResponse() {
    }

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
