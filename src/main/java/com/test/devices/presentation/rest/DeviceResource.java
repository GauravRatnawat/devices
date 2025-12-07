package com.test.devices.presentation.rest;

import com.test.devices.application.dto.CreateDeviceRequest;
import com.test.devices.application.dto.DeviceResponse;
import com.test.devices.application.dto.UpdateDeviceRequest;
import com.test.devices.application.usecase.*;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/api/v1/devices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceResource {

    private static final Logger logger = Logger.getLogger(DeviceResource.class);

    @Inject
    CreateDeviceUseCase createDeviceUseCase;

    @Inject
    GetDeviceUseCase getDeviceUseCase;

    @Inject
    UpdateDeviceUseCase updateDeviceUseCase;

    @Inject
    ListDevicesUseCase listDevicesUseCase;

    @POST
    public Response createDevice(@Valid CreateDeviceRequest request) {
        logger.info("POST /api/v1/devices - Creating device");
        DeviceResponse response = createDeviceUseCase.execute(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @Path("/{id}")
    public Response getDevice(@PathParam("id") Long id) {
        logger.infof("GET /api/v1/devices/%d - Fetching device", id);
        DeviceResponse response = getDeviceUseCase.execute(id);
        return Response.ok(response).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateDevice(@PathParam("id") Long id, @Valid UpdateDeviceRequest request) {
        logger.infof("PUT /api/v1/devices/%d - Updating device", id);
        DeviceResponse response = updateDeviceUseCase.execute(id, request);
        return Response.ok(response).build();
    }

    @GET
    public Response listDevices(
            @QueryParam("brand") String brand,
            @QueryParam("state") String state) {
        logger.info("GET /api/v1/devices - Listing devices");
        List<DeviceResponse> devices = listDevicesUseCase.execute(brand, state != null ? com.test.devices.domain.model.DeviceState.valueOf(state) : null);
        return Response.ok(devices).build();
    }
}
