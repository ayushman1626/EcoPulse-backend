package com.ecopulse.EcoPulse_Backend.dtos.Auth;

import java.util.Map;


public class LoginResponse {

    private Map<String, Object> data;
    public LoginResponse(Map<String, Object> data, boolean status, String message){
      this.data = data;
      this.status = status;
      this.message = message;
    }
    private boolean status;
    private String message;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "data=" + data +
                ", status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
