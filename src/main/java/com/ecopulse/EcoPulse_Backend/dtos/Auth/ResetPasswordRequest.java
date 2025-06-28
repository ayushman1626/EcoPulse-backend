package com.ecopulse.EcoPulse_Backend.dtos.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @Email
    @NotBlank(message = "Email is required")
    private String email;

    private String otp;
    private String oldPassword;

    @NotBlank(message = "New password is required")
    private String newPassword;


    public ResetPasswordRequest() {
    }

    public ResetPasswordRequest(String email, String otp, String newPassword, String oldPassword) {
        this.email = email;
        this.otp = otp;
        this.newPassword = newPassword;
        this.oldPassword = this.oldPassword;
    }
}
