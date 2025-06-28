package com.ecopulse.EcoPulse_Backend.controller;



import com.ecopulse.EcoPulse_Backend.dtos.access.AddAccessRequestDTO;
import com.ecopulse.EcoPulse_Backend.dtos.access.InterfaceAccessDTO;
import com.ecopulse.EcoPulse_Backend.dtos.common.ApiResponse;
import com.ecopulse.EcoPulse_Backend.model.UserPrinciple;
import com.ecopulse.EcoPulse_Backend.service.AccessService;
import com.ecopulse.EcoPulse_Backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;


@RestController
@Tag(name = "Access" , description = "API endpoint for Manage Access")
public class AccessController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccessService accessService;

    @PostMapping("api/interface/{interfaceId}/add-access")
    @Operation(summary = "Give Access")
    public ResponseEntity<ApiResponse<?>> giveAccess(
            @RequestBody AddAccessRequestDTO input,
            @PathVariable UUID interfaceId,
            @AuthenticationPrincipal UserPrinciple userPrinciple
    ) throws AccessDeniedException{
        if(userPrinciple == null){
            return new ResponseEntity<>(
                    new ApiResponse<>(false,"UNAUTHENTICATED",null), HttpStatus.UNAUTHORIZED);
        }
        Boolean accessGiven = accessService.giveAccess(
                interfaceId,input.getUsername(), input.getRole(),userPrinciple.getUsername()
        );

        return new ResponseEntity<>(
                new ApiResponse<>(true,"New user granted access as " + input.getRole(),null),
                HttpStatus.OK);
    }

    @GetMapping("api/interface/{interfaceId}/access")
    @Operation(summary = "Show given access of Interface")
    public ResponseEntity<ApiResponse<List<InterfaceAccessDTO>>> showAccessesByInterface(
            @PathVariable UUID interfaceId,
            @AuthenticationPrincipal UserPrinciple userPrinciple
    )throws AccessDeniedException{
        if(userPrinciple == null){
            return new ResponseEntity<>(
                    new ApiResponse<>(false,"UNAUTHENTICATED",null), HttpStatus.UNAUTHORIZED);
        }

        List<InterfaceAccessDTO> accessList = accessService.getAllAccess(interfaceId,userPrinciple.getUsername());
        return new ResponseEntity<>(new ApiResponse<>(true,"All access fetched successfully",accessList),HttpStatus.OK);
    }

    @DeleteMapping("api/interface/{interfaceId}/access/{username}")
    @Operation(summary = "Remove Access")
    public ResponseEntity<ApiResponse<?>> removeAccess(
            @PathVariable UUID interfaceId,
            @PathVariable String username,
            @AuthenticationPrincipal UserPrinciple userPrinciple
    ) throws AccessDeniedException {
        if(userPrinciple == null){
            return new ResponseEntity<>(
                    new ApiResponse<>(false,"UNAUTHENTICATED",null), HttpStatus.UNAUTHORIZED);
        }

        Boolean accessRemoved = accessService.revokeAccess(interfaceId, username, userPrinciple.getUsername());
        return new ResponseEntity<>(
                new ApiResponse<>(true,"Access removed successfully",null),
                HttpStatus.OK);
    }
}

