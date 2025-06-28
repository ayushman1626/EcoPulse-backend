package com.ecopulse.EcoPulse_Backend.dtos.Auth;

import lombok.Data;

@Data
public class RegistrationResponse {
    private String email;
    private String message;
    private boolean status;

    public RegistrationResponse(String email, String message, boolean status) {
        this.message = message;
        this.status = status;
        this.email = email;
    }

    public boolean isSuccess(){
        return status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RegistrationResponse{" +
                "email='" + email + '\'' +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
