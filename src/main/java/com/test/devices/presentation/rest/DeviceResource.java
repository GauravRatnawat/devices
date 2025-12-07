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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/api/v1/devices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Devices", description = "Device management operations")
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

    @Inject
    DeleteDeviceUseCase deleteDeviceUseCase;

    @POST
    @Operation(
        summary = "Create a new device",
        description = "Creates a new device with the provided information"
    )
    @APIResponses(value = {
        @APIResponse(
            responseCode = "201",
            description = "Device created successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = DeviceResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "400",
            description = "Invalid input data"
        )
    })
    public Response createDevice(@Valid CreateDeviceRequest request) {
        logger.info("POST /api/v1/devices - Creating device");
        DeviceResponse response = createDeviceUseCase.execute(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @Path("/{id}")
    @Operation(
        summary = "Get device by ID",
        description = "Retrieves a device by its unique identifier"
    )
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
            description = "Device found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = DeviceResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Device not found"
        )
    })
    public Response getDevice(
        @Parameter(description = "Device ID", required = true)
        @PathParam("id") Long id) {
        logger.infof("GET /api/v1/devices/%d - Fetching device", id);
        DeviceResponse response = getDeviceUseCase.execute(id);
        return Response.ok(response).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(
        summary = "Update device",
        description = "Updates an existing device with the provided information"
    )
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
            description = "Device updated successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = DeviceResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Device not found"
        ),
        @APIResponse(
            responseCode = "400",
            description = "Invalid input data"
        )
    })
    public Response updateDevice(
        @Parameter(description = "Device ID", required = true)
        @PathParam("id") Long id,
        @Valid UpdateDeviceRequest request) {
        logger.infof("PUT /api/v1/devices/%d - Updating device", id);
        DeviceResponse response = updateDeviceUseCase.execute(id, request);
        return Response.ok(response).build();
    }

    @GET
    @Operation(
        summary = "List all devices",
        description = "Retrieves a list of all devices with optional filtering by brand and state"
    )
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
            description = "List of devices retrieved successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = DeviceResponse.class)
            )
        )
    })
    public Response listDevices(
            @Parameter(description = "Filter by brand name")
            @QueryParam("brand") String brand,
            @Parameter(description = "Filter by device state (ACTIVE, INACTIVE, MAINTENANCE)")
            @QueryParam("state") String state) {
        logger.info("GET /api/v1/devices - Listing devices");
        List<DeviceResponse> devices = listDevicesUseCase.execute(brand, state != null ? com.test.devices.domain.model.DeviceState.valueOf(state) : null);
        return Response.ok(devices).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(
        summary = "Delete device",
        description = "Deletes a device by its unique identifier"
    )
    @APIResponses(value = {
        @APIResponse(
            responseCode = "204",
            description = "Device deleted successfully"
        ),
        @APIResponse(
            responseCode = "404",
            description = "Device not found"
        )
    })
    public Response deleteDevice(
        @Parameter(description = "Device ID", required = true)
        @PathParam("id") Long id) {
        logger.infof("DELETE /api/v1/devices/%d - Deleting device", id);
        deleteDeviceUseCase.execute(id);
        return Response.noContent().build();
    }
}
