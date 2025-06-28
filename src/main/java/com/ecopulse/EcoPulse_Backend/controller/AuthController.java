package com.ecopulse.EcoPulse_Backend.controller;




import com.ecopulse.EcoPulse_Backend.dtos.Auth.*;
import com.ecopulse.EcoPulse_Backend.dtos.common.ApiResponse;
import com.ecopulse.EcoPulse_Backend.model.User;
import com.ecopulse.EcoPulse_Backend.service.AuthService;
import com.ecopulse.EcoPulse_Backend.service.GoogleVerifierService;
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

    @Autowired
    GoogleVerifierService googleVerifierService;


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

    @PostMapping("/register/verify-otp")
    @Operation(summary = "Verify Otp")
    ResponseEntity<ApiResponse<Map<String, Object>>> verifyOtp(@RequestBody Map<String, String> input){

        RegistrationResponse response = authService.verifyOtp(input.get("email"), input.get("otp"));

        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        if (!response.isSuccess()) {
            return ResponseEntity.status(status).body(
                    new ApiResponse<>(false, response.getMessage(), null));
        } else {
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("email", response.getEmail());

            return ResponseEntity.status(status).body(
                    new ApiResponse<>(true, response.getMessage(), responseData));
        }
    }

    @PostMapping("/resend-otp")
    @Operation(summary = "Resend OTP")
    public ResponseEntity<ApiResponse<Map<String, Object>>> resendOtp(@RequestBody Map<String, String> emailData) {
        String response = authService.resendOtp(emailData.get("email"));
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("email", emailData.get("email"));
        return ResponseEntity.ok(new ApiResponse<>(true, response, responseData));
    }


    @PostMapping("/login")
    @Operation(summary = "login")
    ResponseEntity<ApiResponse<Map<String, Object>>> login(
            @Valid @RequestBody LoginRequest request){
        LoginResponse response;
        response = authService.loginUser(request);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true, response.getMessage(), response.getData()));
    }

    //forget-password
    @PostMapping("/forget-password")
    @Operation(summary = "Forget Password")
    ResponseEntity<ApiResponse<Map<String, Object>>> forgetPassword(@RequestBody Map<String, String> input) {
        if (input.get("email") == null || input.get("email").isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, "Email is required", null));
        }
        String response = authService.forgetPassword(input.get("email"));
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("email", input.get("email"));
        return ResponseEntity.ok(new ApiResponse<>(true, response, responseData));
    }


    //reset-password with otp or old password
    @PostMapping("/reset-password")
    @Operation(summary = "Reset Password")
    ResponseEntity<ApiResponse<ResetPasswordRequest>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        if(request.getOtp() == null && request.getOldPassword() == null) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, "Either OTP or Old Password must be provided", null));
        }

        authService.resetPassword(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Password reset successful", request));
    }

    //Google Login
    @PostMapping("/google-login")
    public ResponseEntity<ApiResponse<?>> googleLogin(@RequestHeader("Authorization") String authorizationHeader) {
        // Extract the token from "Bearer <token>"
        String googleIdToken = authorizationHeader.replace("Bearer ", "");

        // Step 2: Verify Google ID token
        if (googleIdToken == null || googleIdToken.isEmpty()) {
            throw new BadCredentialsException("Google ID token is missing or empty");
        }

        GoogleIdToken.Payload payload = googleVerifierService.verifyToken(googleIdToken);
        if(payload == null){
            return new ResponseEntity<>(
                    new ApiResponse<>(false,"Invalid ID token",null), HttpStatus.UNAUTHORIZED);
        }

        // Step 3: Find or create user in DB
        User user = userService.findOrCreateUser(payload);

        // Step 4: Generate JWT for your app
        String appJwt = jwtUtil.generateToken(user.getEmail());

        Map<String, Object> data = new HashMap<>();
        data.put("token",appJwt);
        data.put("email", user.getEmail());
        data.put("fullName",user.getFullName());
        data.put("username",user.getUsername());
        data.put("id",user.getId().toString());

        return new ResponseEntity<>(
                new ApiResponse<>(true, "Google login successful", data), HttpStatus.OK);
    }

}

