package com.test.devices.presentation.exception;

import com.test.devices.application.exception.DeviceValidationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DeviceValidationExceptionMapper implements ExceptionMapper<DeviceValidationException> {

    @Override
    public Response toResponse(DeviceValidationException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                Response.Status.BAD_REQUEST.getStatusCode(),
                exception.getMessage()
        );

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .build();
    }
}

