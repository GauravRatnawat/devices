package com.test.devices.presentation.exception;

import com.test.devices.application.exception.DeviceNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DeviceNotFoundExceptionMapper implements ExceptionMapper<DeviceNotFoundException> {

    @Override
    public Response toResponse(DeviceNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                Response.Status.NOT_FOUND.getStatusCode(),
                exception.getMessage()
        );

        return Response.status(Response.Status.NOT_FOUND)
                .entity(errorResponse)
                .build();
    }
}

