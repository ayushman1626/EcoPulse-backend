package com.ecopulse.EcoPulse_Backend.controller;



import com.ecopulse.EcoPulse_Backend.dtos.SensorReadingDTO;
import com.ecopulse.EcoPulse_Backend.dtos.common.ApiResponse;
import com.ecopulse.EcoPulse_Backend.dtos.device.DeviceDTO;
import com.ecopulse.EcoPulse_Backend.dtos.device.DeviceRequestDTO;
import com.ecopulse.EcoPulse_Backend.model.UserPrinciple;
import com.ecopulse.EcoPulse_Backend.service.DeviceService;
import com.ecopulse.EcoPulse_Backend.util.DeviceStreamManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RequestMapping("api/device")
@RestController
@Tag(name = "Device", description = "API endpoints for Device")
public class DeviceController {

    @Autowired
    DeviceService deviceService;

    @Autowired
    DeviceStreamManager deviceStreamManager;

    Logger logger = LoggerFactory.getLogger(DeviceController.class);

    //Create a new device for a specific interface
    @PostMapping("/{interfaceId}")
    @Operation(summary = "Create Device")
    public ResponseEntity<ApiResponse<DeviceDTO>> createDevice (
            @RequestBody DeviceRequestDTO device,
            @PathVariable UUID interfaceId,
            @AuthenticationPrincipal UserPrinciple userPrinciple
    ) throws Exception{
        if(userPrinciple == null){
            return new ResponseEntity<>(
                    new ApiResponse<>(false,"UNAUTHENTICATED",null), HttpStatus.UNAUTHORIZED);
        }
        DeviceDTO savedDevice = deviceService.createDevice(device, interfaceId,userPrinciple.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Device created successfully", savedDevice));
    }





    //Get all devices for the authenticated user
    @GetMapping("")
    @Operation(summary = "Get All Devices")
    public ResponseEntity<ApiResponse<List<DeviceDTO>>> getDevices(
            @AuthenticationPrincipal UserPrinciple userPrinciple
    ){
        if(userPrinciple == null){
            return new ResponseEntity<>(new ApiResponse<>(false,"UNAUTHENTICATED",null), HttpStatus.UNAUTHORIZED);
        }

        List<DeviceDTO> devices = deviceService.getAllDevices(userPrinciple);

        return ResponseEntity.ok(new ApiResponse<>(
                true, "All devices fetched successfully",devices));
    }

    //Get device by id
    @GetMapping("/{deviceId}")
    @Operation(summary = "Get Device by ID")
    public ResponseEntity<ApiResponse<DeviceDTO>> getDeviceById(
            @PathVariable UUID deviceId,
            @AuthenticationPrincipal UserPrinciple userPrinciple
    ) throws Exception {
        if(userPrinciple == null){
            return new ResponseEntity<>(new ApiResponse<>(false,"UNAUTHENTICATED",null), HttpStatus.UNAUTHORIZED);
        }
        DeviceDTO device = deviceService.getDeviceById(deviceId, userPrinciple.getUsername());
        if (device == null) {
            return new ResponseEntity<>(new ApiResponse<>(false, "Device not found", null), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(new ApiResponse<>(
                true, "Device fetched successfully", device));
    }

    //Get sensor reading from date
    @GetMapping("/{deviceId}/readings")
    @Operation(summary = "Get Device Sensor Readings")
    public ResponseEntity<ApiResponse<List<SensorReadingDTO>>> getDeviceReadings(
            @PathVariable UUID deviceId,
            @RequestParam(value = "d", required = false) Integer days,
            @RequestParam(value = "h", required = false) Integer hours,
            @AuthenticationPrincipal UserPrinciple userPrinciple
    )throws Exception {
        if(userPrinciple == null){
            return new ResponseEntity<>(new ApiResponse<>(false,"UNAUTHENTICATED",null), HttpStatus.UNAUTHORIZED);
        }
        List<SensorReadingDTO> readings = deviceService.getDeviceReadings(deviceId, userPrinciple.getUsername(),days,hours);
        if (readings == null || readings.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse<>(false, "No readings found for this device", null), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(new ApiResponse<>(
                true, "Device readings fetched successfully", readings));
    }

    //SSE endpoint to give sensor data updates
    @GetMapping(value = "/{deviceId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Get Device Sensor Data Stream")
    public SseEmitter streamDeviceData(
            @PathVariable UUID deviceId,
            @AuthenticationPrincipal UserPrinciple userPriciple
    ) throws Exception{
        if(userPriciple == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized");
        }

        try {
            // Check if the device exists and the user has access to it if so return device stream
            DeviceDTO device = deviceService.getDeviceById(deviceId, userPriciple.getUsername());
            if (device == null) {
                throw new EntityNotFoundException("Device not found");
            }
            //logging
            logger.info("User {} requested stream for device {}", userPriciple.getUsername(), deviceId);
            SseEmitter emitter = new SseEmitter(0L);
            deviceStreamManager.register(deviceId,emitter);
            return emitter;
        } catch (EntityNotFoundException e) {
            logger.error("Device not found for ID: {}", deviceId, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AccessDeniedException e) {
            logger.error("Forbidden", deviceId, e);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            logger.error("Error streaming device data for ID: {}", deviceId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error streaming device data");
        }
    }

    //Delete a device by ID
    @DeleteMapping("/{deviceId}")
    @Operation(summary = "Delete Device")
    public ResponseEntity<ApiResponse<String>> deleteDevice(
            @PathVariable UUID deviceId,
            @AuthenticationPrincipal UserPrinciple userPrinciple
    ) throws Exception {
        if(userPrinciple == null){
            return new ResponseEntity<>(new ApiResponse<>(false,"UNAUTHENTICATED",null), HttpStatus.UNAUTHORIZED);
        }
        deviceService.deleteDevice(deviceId, userPrinciple.getUsername());
        return ResponseEntity.ok(new ApiResponse<>(true, "Device deleted successfully", null));
    }
}

