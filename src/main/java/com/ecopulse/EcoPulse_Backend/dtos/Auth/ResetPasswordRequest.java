package com.ecopulse.EcoPulse_Backend.dtos.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
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
        this.oldPassword = oldPassword;
    }

    //getter setters
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }
    public void setOtp(String otp) {
        this.otp = otp;
    }
    public String getOldPassword() {
        return oldPassword;
    }
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    @Override
    public String toString() {
        return "ResetPasswordRequest{" +
                "email='" + email + '\'' +
                ", otp='" + otp + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }

}
