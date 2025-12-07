package com.test.devices.presentation.rest;

import com.test.devices.application.dto.CreateDeviceRequest;
import com.test.devices.application.dto.DeviceResponse;
import com.test.devices.application.usecase.*;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path("/api/v1/devices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceResource {

    private static final Logger logger = Logger.getLogger(DeviceResource.class);

    @Inject
    CreateDeviceUseCase createDeviceUseCase;

    @POST
    public Response createDevice(@Valid CreateDeviceRequest request) {
        logger.info("POST /api/v1/devices - Creating device");
        DeviceResponse response = createDeviceUseCase.execute(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}
