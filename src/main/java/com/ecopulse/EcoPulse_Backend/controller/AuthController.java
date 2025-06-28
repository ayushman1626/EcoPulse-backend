package com.ecopulse.EcoPulse_Backend.controller;




import com.ecopulse.EcoPulse_Backend.dtos.Auth.RegisterRequest;
import com.ecopulse.EcoPulse_Backend.dtos.Auth.RegistrationResponse;
import com.ecopulse.EcoPulse_Backend.dtos.common.ApiResponse;
import com.ecopulse.EcoPulse_Backend.service.AuthService;
import com.ecopulse.EcoPulse_Backend.service.UserService;
import com.ecopulse.EcoPulse_Backend.util.JwtUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.web.bind.annotation.*;



import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequestMapping("api/auth")
@RestController
@Tag(name = "Auth", description = "API endpoints for auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserService userService;


    @PostMapping("/register")
    @Operation(summary = "Register user")
    public ResponseEntity<ApiResponse<Map<String, Object>>> registerUser(
            @Valid @RequestBody RegisterRequest request) {

        //passing to service
        RegistrationResponse response = authService.registerUser(request);

        //response from service
        HttpStatus status = response.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT;

        //api-response
        if (!response.isSuccess()) {
            return ResponseEntity.status(status).body(
                    new ApiResponse<>(false, response.getMessage(), null));
        } else {
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("email", response.getEmail());
            responseData.put("otpExpiresInSeconds", 600);

            return ResponseEntity.status(status).body(
                    new ApiResponse<>(true, response.getMessage(), responseData));
        }
    }
}

