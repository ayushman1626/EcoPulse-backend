package com.ecopulse.EcoPulse_Backend.service;

import com.ecopulse.EcoPulse_Backend.dtos.Auth.RegisterRequest;
import com.ecopulse.EcoPulse_Backend.dtos.Auth.RegistrationResponse;
import com.ecopulse.EcoPulse_Backend.exceptions.UserAlreadyExistsException;
import com.ecopulse.EcoPulse_Backend.model.User;
import com.ecopulse.EcoPulse_Backend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    OtpService otpService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public RegistrationResponse registerUser(RegisterRequest request) throws RuntimeException{

        if(userRepo.findByEmailAndIsVerifiedTrue(request.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("User already exist with the email");
        }
        if(userRepo.findByUsernameAndIsVerifiedTrue(request.getUsername()).isPresent()){
            throw new UserAlreadyExistsException("User already exist with the username");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setVerified(false);
        user.setPassword(encoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        userRepo.save(user);

        // Sending OTP email
        try {
            otpService.generateAndSendOtp(user.getEmail(),"Verify your email");
            return new RegistrationResponse(user.getEmail(), "Registration successful. Verification email sent.", true);
        } catch (Exception e) {
            userRepo.delete(user);
            return new RegistrationResponse(user.getEmail(), "Registration failed. Could not send verification email."+e.getMessage(), false);
        }
    }
}
