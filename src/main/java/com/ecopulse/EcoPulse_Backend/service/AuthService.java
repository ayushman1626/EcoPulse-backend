package com.ecopulse.EcoPulse_Backend.service;

import com.ecopulse.EcoPulse_Backend.dtos.Auth.*;
import com.ecopulse.EcoPulse_Backend.exceptions.UserAlreadyExistsException;
import com.ecopulse.EcoPulse_Backend.model.User;
import com.ecopulse.EcoPulse_Backend.repository.UserRepo;
import com.ecopulse.EcoPulse_Backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    OtpService otpService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;


    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public RegistrationResponse registerUser(RegisterRequest request) throws RuntimeException {

        Optional<User> existingEmailUser = userRepo.findByEmail(request.getEmail());
        if (existingEmailUser.isPresent()) {
            if (existingEmailUser.get().getVerified()) {
                throw new UserAlreadyExistsException("User already exists with the email");
            } else {
                // Resend OTP to unverified user
                try {
                    otpService.generateAndSendOtp(existingEmailUser.get().getEmail(), "Verify your email");
                    return new RegistrationResponse(existingEmailUser.get().getEmail(),
                            "User already registered but not verified. Verification email resent.",
                            true);
                } catch (Exception e) {
                    return new RegistrationResponse(existingEmailUser.get().getEmail(),
                            "Failed to resend verification email: " + e.getMessage(),
                            false);
                }
            }
        }

        Optional<User> existingUsernameUser = userRepo.findByUsername(request.getUsername());
        if (existingUsernameUser.isPresent() && existingUsernameUser.get().getVerified()) {
            throw new UserAlreadyExistsException("User already exists with the username");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setVerified(false);
        user.setPassword(encoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        userRepo.save(user);

        try {
            otpService.generateAndSendOtp(user.getEmail(), "Verify your email");
            return new RegistrationResponse(user.getEmail(),
                    "Registration successful. Verification email sent.",
                    true);
        } catch (Exception e) {
            userRepo.delete(user);
            return new RegistrationResponse(user.getEmail(),
                    "Registration failed. Could not send verification email. " + e.getMessage(),
                    false);
        }
    }


    public RegistrationResponse verifyOtp(String email, String otp) throws BadCredentialsException {
        User user = userRepo.findLatestByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(!otpService.verifyOtp(email,otp)){
            throw new BadCredentialsException("OTP invalid or expired");
        }
        user.setVerified(true);
        user.setOtp(null);
        user.setOtpExpiration(null);
        userRepo.save(user);
        return new RegistrationResponse(user.getEmail(),"OTP verification successful",true);
    }

    public String resendOtp(String email){
        User user = userRepo.findLatestByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return otpService.generateAndSendOtp(user.getEmail(),"Verify your email");
    }



    public LoginResponse loginUser(LoginRequest request) throws BadCredentialsException{

        User user = userRepo.findByEmailAndIsVerifiedTrue(request.getEmail()).orElseThrow(()->
                new BadCredentialsException("NO VERIFIED USER FOUND WITH THAT EMAIL")
        );

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtil.generateToken(user.getEmail());

        Map<String, Object> data = new HashMap<>();
        data.put("token",token);
        data.put("email", user.getEmail());
        data.put("fullName",user.getFullName());
        data.put("username",user.getUsername());
        data.put("id",user.getId().toString());

        return new LoginResponse(data,true,"Login successful");
    }

    // Forget Password
    public String forgetPassword(String email) {
        User user = userRepo.findLatestByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return otpService.generateAndSendOtp(user.getEmail(), "Reset your password");
    }

    // Reset Password
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepo.findLatestByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (request.getOtp() != null) {
            if (!otpService.verifyOtp(user.getEmail(), request.getOtp())) {
                throw new BadCredentialsException("Invalid or expired OTP");
            }
        } else if (request.getOldPassword() != null) {
            if (!encoder.matches(request.getOldPassword(), user.getPassword())) {
                throw new BadCredentialsException("Old password is incorrect");
            }
        } else {
            throw new BadCredentialsException("Either OTP or Old Password must be provided");
        }
        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepo.save(user);
    }
}
